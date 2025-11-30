#!/bin/bash

# AI Cockpit Platform 数据库初始化脚本
# 作者: AI Cockpit Team
# 描述: 初始化MySQL数据库和Redis缓存

set -e

echo "🚀 开始初始化AI驾驶舱平台数据库和缓存..."

# 检查MySQL服务是否运行
if ! mysqladmin ping -h localhost -u root -proot --silent; then
    echo "❌ MySQL服务未运行，请先启动MySQL服务"
    exit 1
fi

echo "✅ MySQL服务正常运行"

# 检查Redis服务是否运行
if ! redis-cli ping > /dev/null 2>&1; then
    echo "❌ Redis服务未运行，请先启动Redis服务"
    exit 1
fi

echo "✅ Redis服务正常运行"

# 执行数据库初始化SQL
echo "📊 正在初始化数据库结构..."
mysql -h localhost -u root -proot < /usr/local/share/auto-ai-pliot-space/auto-ai-pliot/scripts/init-database.sql

echo "✅ 数据库结构初始化完成"

# 测试Redis连接
echo "🔍 测试Redis缓存连接..."
redis-cli set "ai-cockpit:test" "success" > /dev/null
if [ "$(redis-cli get "ai-cockpit:test")" = "success" ]; then
    echo "✅ Redis缓存连接正常"
    redis-cli del "ai-cockpit:test" > /dev/null
else
    echo "❌ Redis缓存连接异常"
    exit 1
fi

# 初始化Redis缓存数据
echo "💾 正在初始化Redis缓存数据..."
redis-cli flushall > /dev/null

# 设置系统配置缓存
redis-cli hset "ai-cockpit:config" "version" "1.0.0" > /dev/null
redis-cli hset "ai-cockpit:config" "name" "AI Cockpit Platform" > /dev/null
redis-cli hset "ai-cockpit:config" "description" "AI驾驶舱平台" > /dev/null

# 设置默认用户权限缓存
redis-cli hset "ai-cockpit:permissions:1" "dashboard" "read,write" > /dev/null
redis-cli hset "ai-cockpit:permissions:1" "model" "read,write,delete" > /dev/null
redis-cli hset "ai-cockpit:permissions:1" "dataset" "read,write,delete" > /dev/null
redis-cli hset "ai-cockpit:permissions:1" "deployment" "read,write,delete" > /dev/null

# 设置API限流配置
redis-cli set "ai-cockpit:rate-limit:api" "1000" > /dev/null
redis-cli set "ai-cockpit:rate-limit:auth" "100" > /dev/null

# 验证缓存数据
echo "🔍 验证缓存数据..."
if [ "$(redis-cli hget "ai-cockpit:config" "version")" = "1.0.0" ]; then
    echo "✅ Redis缓存数据初始化成功"
else
    echo "❌ Redis缓存数据初始化失败"
    exit 1
fi

echo ""
echo "🎉 AI驾驶舱平台数据库和缓存初始化完成！"
echo ""
echo "📊 数据库信息："
echo "   - 数据库名: ai_cockpit"
echo "   - 用户名: root"
echo "   - 密码: root"
echo "   - 主机: localhost:3306"
echo ""
echo "💾 缓存信息："
echo "   - Redis主机: localhost:6379"
echo "   - 密码: 无"
echo "   - 数据库: 0"
echo ""
echo "👤 默认管理员账户："
echo "   - 用户名: admin"
echo "   - 密码: admin123"
echo "   - 邮箱: admin@aicockpit.com"
echo ""
echo "🚀 现在可以启动后端服务了！"