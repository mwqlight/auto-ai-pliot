// 用户相关类型
export interface UserInfo {
  id: number
  username: string
  email: string
  phone: string
  realName: string
  avatar: string
  status: number
  createTime: string
  updateTime: string
}

// 模型相关类型
export interface ModelInfo {
  id: number
  name: string
  description: string
  type: string
  framework: string
  version: string
  parameters: number
  accuracy: number
  status: number
  creatorId: number
  createTime: string
  updateTime: string
}

// 数据集相关类型
export interface DatasetInfo {
  id: number
  name: string
  description: string
  type: string
  format: string
  dataCount: number
  fileSize: number
  status: number
  creatorId: number
  createTime: string
  updateTime: string
}

// 训练任务相关类型
export interface TrainingTask {
  id: number
  name: string
  description: string
  modelId: number
  datasetId: number
  status: number
  progress: number
  startTime: string
  endTime: string
  creatorId: number
  createTime: string
  updateTime: string
}

// 部署相关类型
export interface DeploymentInfo {
  id: number
  name: string
  description: string
  modelId: number
  status: number
  apiEndpoint: string
  environment: string
  creatorId: number
  createTime: string
  updateTime: string
}

// API响应类型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: number
}

// 分页参数
export interface PaginationParams {
  page: number
  size: number
  sort?: string
  order?: 'asc' | 'desc'
}

// 分页响应
export interface PaginationResponse<T> {
  items: T[]
  total: number
  page: number
  size: number
  pages: number
}

// 菜单项类型
export interface MenuItem {
  id: string
  name: string
  path: string
  icon?: string
  children?: MenuItem[]
  permission?: string
}

// 图表数据点
export interface ChartDataPoint {
  name: string
  value: number
}

// 监控指标
export interface MonitorMetric {
  name: string
  value: number
  unit: string
  trend: 'up' | 'down' | 'stable'
}

// 文件上传信息
export interface UploadFileInfo {
  id: string
  name: string
  size: number
  progress: number
  status: 'uploading' | 'success' | 'error'
  url?: string
}