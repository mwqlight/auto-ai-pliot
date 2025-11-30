#!/bin/bash

# AI Cockpit 部署脚本
# 支持本地部署和Docker部署

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        log_error "命令 $1 未安装，请先安装"
        exit 1
    fi
}

# 显示帮助信息
show_help() {
    echo "AI Cockpit 部署脚本"
    echo ""
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  -h, --help          显示此帮助信息"
    echo "  -e, --env <环境>     部署环境 (dev|test|prod)，默认: dev"
    echo "  -m, --mode <模式>    部署模式 (local|docker)，默认: docker"
    echo "  -b, --build         重新构建镜像"
    echo "  -d, --database      初始化数据库"
    echo "  -f, --frontend      只部署前端"
    echo "  -r, --backend       只部署后端"
    echo ""
    echo "示例:"
    echo "  $0 -e prod -m docker -b    # 生产环境Docker部署并重新构建"
    echo "  $0 -m local -d            # 本地部署并初始化数据库"
}

# 默认参数
ENV="dev"
MODE="docker"
BUILD=false
DATABASE=false
FRONTEND_ONLY=false
BACKEND_ONLY=false

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -e|--env)
            ENV="$2"
            shift 2
            ;;
        -m|--mode)
            MODE="$2"
            shift 2
            ;;
        -b|--build)
            BUILD=true
            shift
            ;;
        -d|--database)
            DATABASE=true
            shift
            ;;
        -f|--frontend)
            FRONTEND_ONLY=true
            shift
            ;;
        -r|--backend)
            BACKEND_ONLY=true
            shift
            ;;
        *)
            log_error "未知参数: $1"
            show_help
            exit 1
            ;;
    esac
done

# 验证环境参数
if [[ ! "$ENV" =~ ^(dev|test|prod)$ ]]; then
    log_error "环境参数错误，必须是 dev、test 或 prod"
    exit 1
fi

# 验证模式参数
if [[ ! "$MODE" =~ ^(local|docker)$ ]]; then
    log_error "模式参数错误，必须是 local 或 docker"
    exit 1
fi

# 主部署函数
deploy() {
    log_info "开始部署 AI Cockpit ($ENV 环境, $MODE 模式)"
    
    # 检查必要的命令
    check_command "java"
    check_command "node"
    check_command "npm"
    
    if [[ "$MODE" == "docker" ]]; then
        check_command "docker"
        check_command "docker-compose"
    fi
    
    # 停止现有服务
    stop_services
    
    # 数据库初始化
    if [[ "$DATABASE" == true ]]; then
        init_database
    fi
    
    # 构建
    if [[ "$BUILD" == true ]]; then
        build_services
    fi
    
    # 部署服务
    if [[ "$FRONTEND_ONLY" == true ]]; then
        deploy_frontend
    elif [[ "$BACKEND_ONLY" == true ]]; then
        deploy_backend
    else
        deploy_all
    fi
    
    log_success "部署完成！"
    show_deployment_info
}

# 停止服务
stop_services() {
    log_info "停止现有服务..."
    
    if [[ "$MODE" == "docker" ]]; then
        docker-compose down 2>/dev/null || true
    else
        # 停止本地进程
        pkill -f "java -jar.*ai-cockpit" || true
        pkill -f "npm run dev" || true
    fi
}

# 初始化数据库
init_database() {
    log_info "初始化数据库..."
    
    # 检查MySQL服务
    if ! mysql -h localhost -u root -proot -e "SELECT 1" &>/dev/null; then
        log_error "MySQL服务未启动或连接失败"
        exit 1
    fi
    
    # 执行初始化脚本
    mysql -h localhost -u root -proot < scripts/init-database.sql
    
    log_success "数据库初始化完成"
}

# 构建服务
build_services() {
    log_info "构建服务..."
    
    if [[ "$MODE" == "docker" ]]; then
        # 构建Docker镜像
        log_info "构建后端镜像..."
        docker build -t ai-cockpit-backend:latest ./ai-cockpit-backend
        
        log_info "构建前端镜像..."
        docker build -t ai-cockpit-frontend:latest ./ai-cockpit-frontend
    else
        # 本地构建
        log_info "构建后端..."
        cd ai-cockpit-backend
        mvn clean package -DskipTests
        cd ..
        
        log_info "构建前端..."
        cd ai-cockpit-frontend
        npm run build
        cd ..
    fi
    
    log_success "构建完成"
}

# 部署所有服务
deploy_all() {
    log_info "部署所有服务..."
    
    if [[ "$MODE" == "docker" ]]; then
        # Docker部署
        docker-compose up -d
        
        # 等待服务启动
        wait_for_service "http://localhost:8080/actuator/health" "后端服务"
        wait_for_service "http://localhost:3000" "前端服务"
    else
        # 本地部署
        deploy_backend_local
        deploy_frontend_local
    fi
}

# 部署后端
deploy_backend() {
    log_info "部署后端服务..."
    
    if [[ "$MODE" == "docker" ]]; then
        docker-compose up -d backend mysql redis
        wait_for_service "http://localhost:8080/actuator/health" "后端服务"
    else
        deploy_backend_local
    fi
}

# 部署前端
deploy_frontend() {
    log_info "部署前端服务..."
    
    if [[ "$MODE" == "docker" ]]; then
        docker-compose up -d frontend
        wait_for_service "http://localhost:3000" "前端服务"
    else
        deploy_frontend_local
    fi
}

# 本地部署后端
deploy_backend_local() {
    log_info "启动本地后端服务..."
    
    cd ai-cockpit-backend
    
    # 检查是否已编译
    if [[ ! -f "target/*.jar" ]]; then
        log_warning "后端未编译，开始编译..."
        mvn clean package -DskipTests
    fi
    
    # 启动服务
    nohup java -jar target/*.jar > ../logs/backend.log 2>&1 &
    
    cd ..
    
    wait_for_service "http://localhost:8080/actuator/health" "后端服务"
}

# 本地部署前端
deploy_frontend_local() {
    log_info "启动本地前端服务..."
    
    cd ai-cockpit-frontend
    
    # 安装依赖
    if [[ ! -d "node_modules" ]]; then
        log_info "安装前端依赖..."
        npm install
    fi
    
    # 启动开发服务器
    nohup npm run dev > ../logs/frontend.log 2>&1 &
    
    cd ..
    
    wait_for_service "http://localhost:3000" "前端服务"
}

# 等待服务启动
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30
    local attempt=1
    
    log_info "等待 $service_name 启动..."
    
    while [[ $attempt -le $max_attempts ]]; do
        if curl -s -f $url > /dev/null; then
            log_success "$service_name 启动成功"
            return 0
        fi
        
        log_info "尝试 $attempt/$max_attempts: $service_name 尚未就绪"
        sleep 2
        ((attempt++))
    done
    
    log_error "$service_name 启动超时"
    exit 1
}

# 显示部署信息
show_deployment_info() {
    echo ""
    echo "========================================="
    echo "           AI Cockpit 部署信息"
    echo "========================================="
    echo "环境: $ENV"
    echo "模式: $MODE"
    echo ""
    echo "服务地址:"
    echo "  前端: http://localhost:3000"
    echo "  后端: http://localhost:8080"
    echo "  API文档: http://localhost:8080/swagger-ui.html"
    echo ""
    echo "默认管理员账号:"
    echo "  用户名: admin"
    echo "  密码: admin123"
    echo ""
    echo "日志文件:"
    echo "  后端日志: logs/backend.log"
    echo "  前端日志: logs/frontend.log"
    echo "========================================="
}

# 创建日志目录
mkdir -p logs

# 执行部署
deploy