# AI Cockpit API 接口文档

> 基于 SpringDoc OpenAPI 3.0 的 RESTful API 文档

## 📋 文档概述

本文档描述了 AI Cockpit 平台的所有 RESTful API 接口，包括请求参数、响应格式、错误码等信息。

### 基础信息
- **API 版本**: v1
- **Base URL**: `http://localhost:8080/api/v1`
- **认证方式**: JWT Bearer Token
- **数据格式**: JSON

### 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1650000000000
}
```

### 错误码说明

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 200 | 成功 | 操作成功 |
| 400 | 请求参数错误 | 检查请求参数 |
| 401 | 未授权 | 检查Token是否有效 |
| 403 | 权限不足 | 检查用户权限 |
| 404 | 资源不存在 | 检查请求路径 |
| 500 | 服务器内部错误 | 联系系统管理员 |

## 🔐 认证授权接口

### 用户登录

**POST** `/auth/login`

**请求参数**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_here",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "email": "admin@ai-cockpit.com",
      "nickname": "系统管理员",
      "roles": ["ROLE_SUPER_ADMIN"]
    }
  },
  "timestamp": 1650000000000
}
```

### 刷新Token

**POST** `/auth/refresh`

**请求头**:
```
Authorization: Bearer {refreshToken}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "Token刷新成功",
  "data": {
    "token": "new_access_token_here"
  },
  "timestamp": 1650000000000
}
```

### 用户登出

**POST** `/auth/logout`

**请求头**:
```
Authorization: Bearer {accessToken}
```

## 👥 用户管理接口

### 获取当前用户信息

**GET** `/users/current`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@ai-cockpit.com",
    "nickname": "系统管理员",
    "avatar": "https://example.com/avatar.jpg",
    "phone": "13800138000",
    "status": 1,
    "lastLoginTime": "2024-01-01 10:00:00",
    "createTime": "2024-01-01 00:00:00",
    "updateTime": "2024-01-01 10:00:00"
  },
  "timestamp": 1650000000000
}
```

### 分页查询用户列表

**GET** `/users`

**查询参数**:
- `page` (可选): 页码，默认1
- `size` (可选): 每页大小，默认10
- `username` (可选): 用户名模糊查询
- `status` (可选): 状态筛选

**响应示例**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "items": [
      {
        "id": 1,
        "username": "admin",
        "email": "admin@ai-cockpit.com",
        "nickname": "系统管理员",
        "status": 1,
        "createTime": "2024-01-01 00:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10,
    "pages": 1
  },
  "timestamp": 1650000000000
}
```

### 创建用户

**POST** `/users`

**请求参数**:
```json
{
  "username": "newuser",
  "email": "newuser@ai-cockpit.com",
  "password": "password123",
  "nickname": "新用户",
  "phone": "13800138001"
}
```

### 更新用户信息

**PUT** `/users/{id}`

**请求参数**:
```json
{
  "nickname": "更新后的昵称",
  "email": "updated@ai-cockpit.com",
  "phone": "13800138002"
}
```

## 🎭 角色管理接口

### 分页查询角色列表

**GET** `/roles`

**查询参数**:
- `page` (可选): 页码，默认1
- `size` (可选): 每页大小，默认10
- `name` (可选): 角色名称模糊查询
- `status` (可选): 状态筛选

### 创建角色

**POST** `/roles`

**请求参数**:
```json
{
  "name": "数据分析师",
  "code": "ROLE_DATA_ANALYST",
  "description": "负责数据分析工作的角色",
  "permissionIds": [1, 2, 3]
}
```

### 更新角色权限

**PUT** `/roles/{id}/permissions`

**请求参数**:
```json
{
  "permissionIds": [1, 2, 3, 4, 5]
}
```

## 🤖 模型管理接口

### 分页查询模型列表

**GET** `/models`

**查询参数**:
- `page` (可选): 页码，默认1
- `size` (可选): 每页大小，默认10
- `name` (可选): 模型名称模糊查询
- `type` (可选): 模型类型筛选
- `status` (可选): 状态筛选

**响应示例**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "items": [
      {
        "id": 1,
        "name": "图像分类模型",
        "version": "v1.0",
        "modelType": "IMAGE_CLASSIFICATION",
        "framework": "TensorFlow",
        "description": "用于图像分类的深度学习模型",
        "status": 2,
        "accuracy": 0.9567,
        "createTime": "2024-01-01 00:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10,
    "pages": 1
  },
  "timestamp": 1650000000000
}
```

### 创建模型

**POST** `/models`

**请求参数**:
```json
{
  "name": "新模型",
  "version": "v1.0",
  "modelType": "TEXT_CLASSIFICATION",
  "framework": "PyTorch",
  "description": "新的文本分类模型"
}
```

### 模型训练

**POST** `/models/{id}/train`

**请求参数**:
```json
{
  "datasetId": 1,
  "epochs": 10,
  "batchSize": 32,
  "learningRate": 0.001
}
```

### 模型部署

**POST** `/models/{id}/deploy`

**响应示例**:
```json
{
  "code": 200,
  "message": "部署成功",
  "data": {
    "endpoint": "http://localhost:8080/api/v1/models/1/predict",
    "status": "DEPLOYED"
  },
  "timestamp": 1650000000000
}
```

### 模型预测

**POST** `/models/{id}/predict`

**请求参数**:
```json
{
  "input": "需要预测的数据"
}
```

## 📊 数据集管理接口

### 分页查询数据集列表

**GET** `/datasets`

**查询参数**:
- `page` (可选): 页码，默认1
- `size` (可选): 每页大小，默认10
- `name` (可选): 数据集名称模糊查询
- `dataType` (可选): 数据类型筛选
- `status` (可选): 状态筛选

### 上传数据集

**POST** `/datasets/upload`

**Content-Type**: `multipart/form-data`

**请求参数**:
- `file` (File): 数据集文件
- `name` (String): 数据集名称
- `description` (String): 数据集描述
- `dataType` (String): 数据类型

### 数据集详情

**GET** `/datasets/{id}`

## 🚀 应用管理接口

### 分页查询应用列表

**GET** `/applications`

**查询参数**:
- `page` (可选): 页码，默认1
- `size` (可选): 每页大小，默认10
- `name` (可选): 应用名称模糊查询
- `appType` (可选): 应用类型筛选
- `status` (可选): 状态筛选

### 创建应用

**POST** `/applications`

**请求参数**:
```json
{
  "name": "智能客服机器人",
  "description": "基于AI的智能客服应用",
  "appType": "CHATBOT",
  "modelId": 1,
  "config": {
    "welcomeMessage": "欢迎使用智能客服",
    "timeout": 300
  }
}
```

### 应用部署

**POST** `/applications/{id}/deploy`

## 📈 监控统计接口

### 系统资源监控

**GET** `/monitor/system`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "cpuUsage": 15.2,
    "memoryUsage": 45.6,
    "diskUsage": 23.8,
    "networkIn": 1024000,
    "networkOut": 512000,
    "monitorTime": "2024-01-01 10:00:00"
  },
  "timestamp": 1650000000000
}
```

### 应用使用统计

**GET** `/monitor/statistics`

**查询参数**:
- `startTime` (可选): 开始时间
- `endTime` (可选): 结束时间
- `type` (可选): 统计类型 (daily, weekly, monthly)

## 🔧 工具类接口

### 文件上传

**POST** `/utils/upload`

**Content-Type**: `multipart/form-data`

**请求参数**:
- `file` (File): 上传的文件

**响应示例**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "filename": "example.jpg",
    "url": "/uploads/example.jpg",
    "size": 1024000
  },
  "timestamp": 1650000000000
}
```

### 数据字典

**GET** `/utils/dict/{type}`

**响应示例**:
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "value": "TEXT_CLASSIFICATION",
      "label": "文本分类"
    },
    {
      "value": "IMAGE_CLASSIFICATION", 
      "label": "图像分类"
    }
  ],
  "timestamp": 1650000000000
}
```

## 🛡️ 权限控制

所有接口都支持基于角色的权限控制，使用 `@PreAuthorize` 注解。

### 权限示例

```java
// 需要管理员权限
@PreAuthorize("hasRole('ROLE_ADMIN')")

// 需要特定权限
@PreAuthorize("hasPermission('user:delete')")

// 允许公开访问
@PreAuthorize("permitAll()")
```

## 🔄 WebSocket 实时通信

### 训练进度推送

**连接地址**: `ws://localhost:8080/ws/training/{taskId}`

**消息格式**:
```json
{
  "type": "progress",
  "data": {
    "taskId": 1,
    "progress": 75.5,
    "epoch": 8,
    "totalEpochs": 10,
    "loss": 0.1234,
    "accuracy": 0.9567
  }
}
```

### 系统监控推送

**连接地址**: `ws://localhost:8080/ws/monitor`

## 📝 使用示例

### JavaScript 调用示例

```javascript
// 登录
const login = async (username, password) => {
  const response = await fetch('/api/v1/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ username, password })
  });
  
  const result = await response.json();
  if (result.code === 200) {
    localStorage.setItem('token', result.data.token);
    return result.data;
  } else {
    throw new Error(result.message);
  }
};

// 带认证的请求
const fetchWithAuth = async (url, options = {}) => {
  const token = localStorage.getItem('token');
  
  const config = {
    ...options,
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      ...options.headers
    }
  };
  
  const response = await fetch(url, config);
  
  if (response.status === 401) {
    // Token过期，刷新或重新登录
    localStorage.removeItem('token');
    window.location.href = '/login';
    return;
  }
  
  return response.json();
};

// 获取用户列表
const getUsers = async (page = 1, size = 10) => {
  return await fetchWithAuth(`/api/v1/users?page=${page}&size=${size}`);
};
```

### Python 调用示例

```python
import requests
import json

class AIClient:
    def __init__(self, base_url="http://localhost:8080/api/v1"):
        self.base_url = base_url
        self.token = None
    
    def login(self, username, password):
        response = requests.post(
            f"{self.base_url}/auth/login",
            json={"username": username, "password": password}
        )
        
        result = response.json()
        if result["code"] == 200:
            self.token = result["data"]["token"]
            return result["data"]
        else:
            raise Exception(result["message"])
    
    def _request(self, method, endpoint, **kwargs):
        headers = kwargs.get('headers', {})
        if self.token:
            headers['Authorization'] = f'Bearer {self.token}'
        
        headers['Content-Type'] = 'application/json'
        kwargs['headers'] = headers
        
        response = requests.request(method, f"{self.base_url}{endpoint}", **kwargs)
        return response.json()
    
    def get_models(self, page=1, size=10):
        return self._request('GET', f'/models?page={page}&size={size}')
    
    def predict(self, model_id, input_data):
        return self._request('POST', f'/models/{model_id}/predict', 
                           json={"input": input_data})

# 使用示例
client = AIClient()
client.login("admin", "admin123")
models = client.get_models()
print(models)
```

## 🔍 在线文档

启动后端服务后，可以通过以下地址访问交互式API文档：

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 📞 技术支持

如有API使用问题，请联系：
- 邮箱: dev@ai-cockpit.com
- 项目主页: https://github.com/your-org/ai-cockpit