import { request } from '@/api'
import type { ModelInfo, PaginationParams, PaginationResponse } from '@/types'

// 获取模型列表
export const getModelList = (params: PaginationParams & { 
  name?: string; 
  type?: string; 
  status?: number 
}) => {
  return request.get<PaginationResponse<ModelInfo>>('/models', { params })
}

// 根据ID获取模型信息
export const getModelById = (id: number) => {
  return request.get<ModelInfo>(`/models/${id}`)
}

// 创建模型
export const createModel = (data: {
  name: string
  description: string
  type: string
  framework: string
  version: string
  parameters: number
}) => {
  return request.post('/models', data)
}

// 更新模型信息
export const updateModel = (id: number, data: Partial<ModelInfo>) => {
  return request.put(`/models/${id}`, data)
}

// 删除模型
export const deleteModel = (id: number) => {
  return request.delete(`/models/${id}`)
}

// 上传模型文件
export const uploadModelFile = (id: number, file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request.post(`/models/${id}/upload`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 下载模型文件
export const downloadModelFile = (id: number) => {
  return request.get(`/models/${id}/download`, {
    responseType: 'blob'
  })
}

// 发布模型
export const publishModel = (id: number) => {
  return request.put(`/models/${id}/publish`)
}

// 取消发布模型
export const unpublishModel = (id: number) => {
  return request.put(`/models/${id}/unpublish`)
}

// 获取公开模型列表
export const getPublicModels = (params: PaginationParams) => {
  return request.get<PaginationResponse<ModelInfo>>('/models/public', { params })
}

// 导入模型
export const importModel = (data: {
  name: string
  description: string
  type: string
  framework: string
  version: string
  modelUrl: string
}) => {
  return request.post('/models/import', data)
}