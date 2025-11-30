#!/bin/bash

# AI Cockpit Platform 数据库迁移脚本
# 作者: AI Cockpit Team
# 描述: 数据库版本管理和迁移工具

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 脚本配置
MIGRATION_DIR="/usr/local/share/auto-ai-pliot-space/auto-ai-pliot/scripts/migrations"
DB_NAME="ai_cockpit"
DB_USER="root"
DB_PASS="root"
DB_HOST="localhost"
DB_PORT="3306"

# 创建迁移目录
mkdir -p "$MIGRATION_DIR"

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

# 检查数据库是否存在
check_database_exists() {
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" -e "USE $DB_NAME;" > /dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

# 创建版本管理表
create_version_table() {
    log_info "创建版本管理表..."
    mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" "$DB_NAME" << EOF
CREATE TABLE IF NOT EXISTS db_version (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    applied_by VARCHAR(100) DEFAULT 'migration-tool'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
EOF
    log_success "版本管理表创建完成"
}

# 获取当前数据库版本
get_current_version() {
    if ! check_database_exists; then
        echo "0.0.0"
        return
    fi
    
    local version=$(mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" -N -s "$DB_NAME" -e "SELECT version FROM db_version ORDER BY applied_at DESC LIMIT 1;" 2>/dev/null || echo "0.0.0")
    echo "${version:-0.0.0}"
}

# 应用迁移文件
apply_migration() {
    local migration_file="$1"
    local version="$2"
    local description="$3"
    
    log_info "应用迁移文件: $migration_file (版本: $version)"
    
    # 执行迁移SQL
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" "$DB_NAME" < "$migration_file"; then
        # 记录迁移版本
        mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" "$DB_NAME" << EOF
INSERT INTO db_version (version, description) VALUES ('$version', '$description');
EOF
        log_success "迁移文件 $migration_file 应用成功"
    else
        log_error "迁移文件 $migration_file 应用失败"
        exit 1
    fi
}

# 创建新的迁移文件
create_migration() {
    local version="$1"
    local description="$2"
    
    local timestamp=$(date +%Y%m%d%H%M%S)
    local migration_file="$MIGRATION_DIR/${timestamp}_${version}.sql"
    
    # 创建迁移文件模板
    cat > "$migration_file" << EOF
-- AI Cockpit Platform 数据库迁移脚本
-- 版本: $version
-- 描述: $description
-- 创建时间: $(date)

-- 迁移前检查
-- SELECT '开始迁移版本: $version' AS '迁移信息';

-- ========== 迁移内容开始 ==========

-- 示例：添加新表
-- CREATE TABLE IF NOT EXISTS new_table (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(100) NOT NULL,
--     created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- 示例：修改表结构
-- ALTER TABLE existing_table ADD COLUMN new_column VARCHAR(255);

-- 示例：插入初始数据
-- INSERT IGNORE INTO config_table (key, value) VALUES 
-- ('new_config_key', 'new_config_value');

-- ========== 迁移内容结束 ==========

-- 迁移后验证
-- SELECT '迁移版本: $version 完成' AS '迁移结果';
EOF
    
    log_success "创建迁移文件: $migration_file"
    echo "请编辑文件: $migration_file 并添加实际的迁移内容"
}

# 列出所有迁移文件
list_migrations() {
    log_info "可用的迁移文件:"
    if [ -d "$MIGRATION_DIR" ] && [ "$(ls -A "$MIGRATION_DIR")" ]; then
        ls -la "$MIGRATION_DIR/"*.sql 2>/dev/null | while read file; do
            echo "  - $(basename "$file")"
        done
    else
        log_warning "迁移目录为空"
    fi
}

# 执行所有待处理的迁移
migrate() {
    log_info "开始数据库迁移..."
    check_database_connection
    create_version_table
    
    local current_version=$(get_current_version)
    log_info "当前数据库版本: $current_version"
    
    # 查找所有迁移文件并按版本排序
    local migration_files=$(find "$MIGRATION_DIR" -name "*.sql" -type f | sort)
    
    if [ -z "$migration_files" ]; then
        log_warning "没有找到迁移文件"
        return
    fi
    
    local applied_count=0
    
    for migration_file in $migration_files; do
        local filename=$(basename "$migration_file")
        local version=$(echo "$filename" | grep -oE '[0-9]+\\.[0-9]+\\.[0-9]+' | head -1)
        
        if [ -z "$version" ]; then
            log_warning "跳过无效的迁移文件: $filename (无法解析版本号)"
            continue
        fi
        
        # 检查是否已经应用过这个版本
        local applied_version=$(mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" -N -s "$DB_NAME" -e "SELECT version FROM db_version WHERE version='$version';" 2>/dev/null)
        
        if [ -n "$applied_version" ]; then
            log_info "跳过已应用的迁移: $version"
            continue
        fi
        
        # 应用迁移
        apply_migration "$migration_file" "$version" "自动迁移: $filename"
        ((applied_count++))
    done
    
    if [ $applied_count -eq 0 ]; then
        log_info "没有需要应用的迁移"
    else
        log_success "成功应用 $applied_count 个迁移文件"
    fi
    
    local new_version=$(get_current_version)
    log_success "数据库迁移完成，当前版本: $new_version"
}

# 回滚到指定版本
rollback() {
    local target_version="${1:-0.0.0}"
    
    log_info "回滚数据库到版本: $target_version"
    check_database_connection
    
    # 获取需要回滚的版本列表
    local versions_to_rollback=$(mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" -N -s "$DB_NAME" -e "SELECT version FROM db_version WHERE version > '$target_version' ORDER BY applied_at DESC;" 2>/dev/null)
    
    if [ -z "$versions_to_rollback" ]; then
        log_info "没有需要回滚的版本"
        return
    fi
    
    log_warning "将要回滚以下版本: $versions_to_rollback"
    read -p "确认回滚？(y/N): " confirm
    
    if [[ ! $confirm =~ ^[Yy]$ ]]; then
        log_info "取消回滚操作"
        return
    fi
    
    # 执行回滚（这里需要根据实际情况实现回滚逻辑）
    log_warning "回滚功能需要根据具体迁移内容实现"
    log_info "请手动处理回滚操作"
}

# 显示帮助信息
show_help() {
    echo "AI Cockpit Platform 数据库迁移工具"
    echo ""
    echo "用法: $0 [命令] [参数]"
    echo ""
    echo "命令:"
    echo "  migrate             执行所有待处理的迁移"
    echo "  create <版本> <描述> 创建新的迁移文件"
    echo "  list                列出所有迁移文件"
    echo "  rollback <版本>     回滚到指定版本"
    echo "  status              显示当前数据库状态"
    echo "  help                显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 migrate"
    echo "  $0 create 1.1.0 "添加新功能表""
    echo "  $0 rollback 1.0.0"
    echo "  $0 status"
}

# 显示数据库状态
show_status() {
    check_database_connection
    
    if check_database_exists; then
        log_success "数据库 $DB_NAME 存在"
        local current_version=$(get_current_version)
        log_info "当前数据库版本: $current_version"
        
        # 显示表信息
        log_info "数据库表信息:"
        mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" -t "$DB_NAME" -e "SHOW TABLES;" 2>/dev/null
        
        # 显示迁移历史
        log_info "迁移历史:"
        mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASS" -t "$DB_NAME" -e "SELECT version, description, applied_at FROM db_version ORDER BY applied_at DESC;" 2>/dev/null
    else
        log_error "数据库 $DB_NAME 不存在"
    fi
}

# 主函数
main() {
    local command="${1:-help}"
    
    case "$command" in
        migrate)
            migrate
            ;;
        create)
            if [ $# -lt 3 ]; then
                log_error "创建迁移文件需要版本号和描述参数"
                echo "用法: $0 create <版本> <描述>"
                exit 1
            fi
            create_migration "$2" "${*:3}"
            ;;
        list)
            list_migrations
            ;;
        rollback)
            rollback "$2"
            ;;
        status)
            show_status
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