# AI Cockpit - 智能AI管理平台

> 基于SpringBoot + Vue3的智能AI模型管理与应用平台

## 🚀 项目简介

AI Cockpit是一个现代化的智能AI模型管理与应用平台，提供从模型训练、数据集管理到业务应用部署的全生命周期管理。

### ✨ 核心特性

- **模型生命周期管理** - 完整的模型训练、评估、部署流程
- **数据集管理** - 支持多种数据格式，智能数据预处理
- **业务应用市场** - 快速构建AI驱动的业务应用
- **资源监控中心** - 实时监控系统资源使用情况
- **权限管理体系** - 基于RBAC的精细化权限控制
- **高科技风格界面** - 现代化UI设计，响应式布局

## 🏗️ 技术架构

### 后端技术栈
- **框架**: SpringBoot 3.0+
- **数据库**: MySQL 8.0
- **缓存**: Redis 7.0
- **ORM**: MyBatis-Plus 3.5+
- **安全**: Spring Security + JWT
- **文档**: SpringDoc OpenAPI 3.0

### 前端技术栈
- **框架**: Vue 3 + TypeScript
- **构建工具**: Vite 4.0+
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **样式**: CSS3 + 科技风格主题

## 📦 项目结构

```
auto-ai-pliot/
├── ai-cockpit-backend/          # 后端SpringBoot项目
│   ├── src/main/java/com/ai/cockpit/
│   │   ├── config/              # 配置类
│   │   ├── controller/          # 控制器层
│   │   ├── dto/                 # 数据传输对象
│   │   ├── entity/              # 实体类
│   │   ├── repository/          # 数据访问层
│   │   ├── service/            # 业务逻辑层
│   │   └── util/                # 工具类
│   └── src/main/resources/      # 配置文件
└── ai-cockpit-frontend/         # 前端Vue3项目
    ├── src/
    │   ├── api/                 # API接口
    │   ├── assets/              # 静态资源
    │   ├── components/          # 组件库
    │   ├── router/              # 路由配置
    │   ├── store/               # 状态管理
    │   ├── styles/              # 样式文件
    │   ├── types/               # 类型定义
    │   └── views/               # 页面视图
    └── package.json
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 7.0+

### 1. 后端启动

```bash
cd ai-cockpit-backend

# 配置数据库
# 修改 application.yml 中的数据库连接信息

# 编译项目
mvn clean compile

# 启动服务
mvn spring-boot:run
```

后端服务默认运行在：http://localhost:8080

### 2. 前端启动

```bash
cd ai-cockpit-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务默认运行在：http://localhost:3000

## 📊 功能模块

### 核心模块

| 模块名称 | 功能描述 | 状态 |
|---------|---------|------|
| 用户认证授权 | JWT认证、权限控制 | ✅ 已完成 |
| 模型生命周期管理 | 模型训练、评估、部署 | ✅ 已完成 |
| 数据集管理 | 数据上传、预处理、版本管理 | ✅ 已完成 |
| 业务应用市场 | AI应用创建、部署、监控 | ✅ 已完成 |
| 资源监控中心 | 系统资源实时监控 | ✅ 已完成 |
| 权限管理体系 | 角色权限精细化管理 | ✅ 已完成 |

### API文档

启动后端服务后，访问以下地址查看API文档：
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 🔧 配置说明

### 数据库配置

在 `ai-cockpit-backend/src/main/resources/application.yml` 中配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_cockpit?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### Redis配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
```

## 🐳 Docker部署

### 构建镜像

```bash
# 构建后端镜像
cd ai-cockpit-backend
docker build -t ai-cockpit-backend:latest .

# 构建前端镜像
cd ai-cockpit-frontend
docker build -t ai-cockpit-frontend:latest .
```

### 使用Docker Compose

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 停止服务
docker-compose down
```

## 📈 监控与日志

### 系统监控
- 实时CPU、内存、磁盘使用率监控
- 数据库连接池监控
- API请求性能监控

### 日志配置
- 日志级别：INFO、DEBUG、ERROR
- 日志文件轮转：按天分割
- 日志格式：JSON格式，便于ELK收集

## 🔒 安全配置

### 认证机制
- JWT Token认证
- Token自动续期
- 密码BCrypt加密

### 权限控制
- 基于角色的访问控制（RBAC）
- 接口级别权限控制
- 数据权限隔离

## 🤝 开发规范

### 代码规范
- 后端：遵循阿里Java开发规范
- 前端：ESLint + Prettier代码格式化
- Git提交：Conventional Commits规范

### API设计规范
- RESTful API设计
- 统一响应格式
- 标准化错误码

## 🐛 常见问题

### Q: 启动时数据库连接失败？
A: 检查MySQL服务是否启动，数据库配置是否正确

### Q: 前端编译报错？
A: 检查Node.js版本，删除node_modules后重新安装依赖

### Q: Redis连接失败？
A: 检查Redis服务是否启动，配置是否正确

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👥 贡献指南

欢迎提交Issue和Pull Request！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 📞 联系方式

- 项目主页：https://github.com/your-org/ai-cockpit
- 问题反馈：https://github.com/your-org/ai-cockpit/issues
- 邮箱：dev@ai-cockpit.com

---

**AI Cockpit - 让AI管理更简单，让智能触手可及**
