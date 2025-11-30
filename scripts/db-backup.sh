#!/bin/bash

# AI Cockpit Platform 数据库备份脚本
# 作者: AI Cockpit Team
# 描述: 数据库备份和恢复工具

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 脚本配置
BACKUP_DIR="/usr/local/share/auto-ai-pliot-space/auto-ai-pliot/backups"
DB_NAME="ai_cockpit"
DB_USER="root"
DB_PASS="root"
DB_HOST="localhost"
DB_PORT="3306"

# 创建备份目录
mkdir -p "$BACKUP_DIR"

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查数据库连接
check_database_connection() {
    if ! mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" -e "SELECT 1;" > /dev/null 2>&1; then
        log_error "无法连接到数据库，请检查配置"
        exit 1
    fi
    log_success "数据库连接正常"
}

# 创建完整备份
create_backup() {
    local backup_type="${1:-full}"
    local timestamp=$(date +%Y%m%d_%H%M%S)
    local backup_file="$BACKUP_DIR/${backup_type}_backup_${timestamp}.sql"
    
    log_info "创建数据库备份: $backup_file"
    check_database_connection
    
    # 使用mysqldump创建备份
    if mysqldump -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" \
        --single-transaction \
        --routines \
        --triggers \
        --events \
        --add-drop-database \
        --databases "$DB_NAME" > "$backup_file"; then
        
        # 压缩备份文件
        gzip "$backup_file"
        local compressed_file="${backup_file}.gz"
        
        local file_size=$(du -h "$compressed_file" | cut -f1)
        log_success "备份创建成功: $compressed_file (大小: $file_size)"
        
        # 清理7天前的备份文件
        cleanup_old_backups
        
        return 0
    else
        log_error "备份创建失败"
        rm -f "$backup_file"
        return 1
    fi
}

# 创建增量备份（需要启用二进制日志）
create_incremental_backup() {
    log_warning "增量备份需要MySQL二进制日志支持，当前使用完整备份替代"
    create_backup "incremental"
}

# 恢复数据库
restore_backup() {
    local backup_file="$1"
    
    if [ -z "$backup_file" ]; then
        # 如果没有指定备份文件，使用最新的备份
        backup_file=$(ls -t "$BACKUP_DIR/"*.sql.gz 2>/dev/null | head -1)
        
        if [ -z "$backup_file" ]; then
            log_error "没有找到备份文件"
            return 1
        fi
        
        log_info "使用最新备份文件: $backup_file"
    fi
    
    if [ ! -f "$backup_file" ]; then
        log_error "备份文件不存在: $backup_file"
        return 1
    fi
    
    log_warning "即将恢复数据库，这将覆盖现有数据！"
    read -p "确认恢复？(y/N): " confirm
    
    if [[ ! $confirm =~ ^[Yy]$ ]]; then
        log_info "取消恢复操作"
        return 0
    fi
    
    log_info "开始恢复数据库..."
    check_database_connection
    
    # 解压备份文件
    local temp_file=$(mktemp)
    gunzip -c "$backup_file" > "$temp_file"
    
    # 恢复数据库
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" < "$temp_file"; then
        log_success "数据库恢复成功"
    else
        log_error "数据库恢复失败"
    fi
    
    # 清理临时文件
    rm -f "$temp_file"
}

# 清理旧备份文件
cleanup_old_backups() {
    local retention_days=7
    log_info "清理 $retention_days 天前的备份文件..."
    
    find "$BACKUP_DIR" -name "*.sql.gz" -type f -mtime +$retention_days -delete
    
    log_success "备份文件清理完成"
}

# 列出所有备份文件
list_backups() {
    log_info "可用的备份文件:"
    if [ -d "$BACKUP_DIR" ] && [ "$(ls -A "$BACKUP_DIR")" ]; then
        ls -la "$BACKUP_DIR/"*.sql.gz 2>/dev/null | while read file; do
            local size=$(du -h "$file" | cut -f1)
            local date=$(stat -f "%Sm" "$file" 2>/dev/null || stat -c "%y" "$file" 2>/dev/null)
            echo "  - $(basename "$file") (大小: $size, 日期: $date)"
        done
    else
        log_warning "备份目录为空"
    fi
}

# 检查备份完整性
check_backup_integrity() {
    local backup_file="$1"
    
    if [ -z "$backup_file" ]; then
        backup_file=$(ls -t "$BACKUP_DIR/"*.sql.gz 2>/dev/null | head -1)
    fi
    
    if [ ! -f "$backup_file" ]; then
        log_error "备份文件不存在: $backup_file"
        return 1
    fi
    
    log_info "检查备份文件完整性: $backup_file"
    
    # 检查文件是否完整
    if gunzip -t "$backup_file"; then
        log_success "备份文件完整性检查通过"
        return 0
    else
        log_error "备份文件损坏"
        return 1
    fi
}

# 自动备份调度（可用于cron job）
auto_backup() {
    log_info "执行自动备份..."
    
    # 检查磁盘空间
    local available_space=$(df "$BACKUP_DIR" | awk 'NR==2 {print $4}')
    local min_space=1048576  # 1GB
    
    if [ $available_space -lt $min_space ]; then
        log_error "磁盘空间不足，无法执行备份"
        return 1
    fi
    
    # 执行备份
    if create_backup "auto"; then
        # 发送备份成功通知（可集成邮件或消息通知）
        log_success "自动备份完成"
        return 0
    else
        log_error "自动备份失败"
        return 1
    fi
}

# 显示帮助信息
show_help() {
    echo "AI Cockpit Platform 数据库备份工具"
    echo ""
    echo "用法: $0 [命令] [参数]"
    echo ""
    echo "命令:"
    echo "  backup [类型]        创建数据库备份 (类型: full|incremental, 默认: full)"
    echo "  restore [文件]       恢复数据库备份"
    echo "  list                 列出所有备份文件"
    echo "  check [文件]         检查备份文件完整性"
    echo "  auto                 执行自动备份（用于cron job）"
    echo "  cleanup              清理旧备份文件"
    echo "  help                 显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 backup full"
    echo "  $0 restore"
    echo "  $0 list"
    echo "  $0 check"
    echo "  $0 auto"
}

# 主函数
main() {
    local command="${1:-help}"
    
    case "$command" in
        backup)
            create_backup "$2"
            ;;
        restore)
            restore_backup "$2"
            ;;
        list)
            list_backups
            ;;
        check)
            check_backup_integrity "$2"
            ;;
        auto)
            auto_backup
            ;;
        cleanup)
            cleanup_old_backups
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            log_error "未知命令: $command"
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"