import axios from 'axios'
import { ElMessage } from 'element-plus'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import type { ApiResponse } from '@/types'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 添加token到请求头
    const token = localStorage.getItem('token')
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res: ApiResponse = response.data
    
    // 请求成功
    if (res.code === 200) {
      return res.data
    }
    
    // 处理业务错误
    if (res.code === 401) {
      // 未授权，跳转到登录页
      localStorage.removeItem('token')
      window.location.href = '/login'
      return Promise.reject(new Error('未授权，请重新登录'))
    }
    
    if (res.code === 403) {
      ElMessage.error('权限不足')
      return Promise.reject(new Error('权限不足'))
    }
    
    // 其他业务错误
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  (error) => {
    // 处理网络错误
    if (error.code === 'ECONNABORTED') {
      ElMessage.error('请求超时，请检查网络连接')
    } else if (error.response?.status === 404) {
      ElMessage.error('请求的资源不存在')
    } else if (error.response?.status === 500) {
      ElMessage.error('服务器内部错误')
    } else {
      ElMessage.error('网络错误，请稍后重试')
    }
    
    return Promise.reject(error)
  }
)

// 通用请求方法
export const request = {
  get: <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    return service.get(url, config)
  },
  
  post: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
    return service.post(url, data, config)
  },
  
  put: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
    return service.put(url, data, config)
  },
  
  delete: <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    return service.delete(url, config)
  },
  
  patch: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
    return service.patch(url, data, config)
  }
}

export default service