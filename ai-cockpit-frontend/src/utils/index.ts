import { ElMessage, ElMessageBox } from 'element-plus'
import type { Action } from 'element-plus'

// 格式化文件大小
export const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B'
  
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化时间
export const formatTime = (date: string | Date): string => {
  const d = new Date(date)
  return d.toLocaleString('zh-CN')
}

// 格式化相对时间
export const formatRelativeTime = (date: string | Date): string => {
  const d = new Date(date)
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const month = 30 * day
  const year = 365 * day
  
  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return Math.floor(diff / minute) + '分钟前'
  } else if (diff < day) {
    return Math.floor(diff / hour) + '小时前'
  } else if (diff < month) {
    return Math.floor(diff / day) + '天前'
  } else if (diff < year) {
    return Math.floor(diff / month) + '个月前'
  } else {
    return Math.floor(diff / year) + '年前'
  }
}

// 深拷贝
export const deepClone = <T>(obj: T): T => {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj.getTime()) as T
  if (obj instanceof Array) return obj.map(item => deepClone(item)) as T
  
  const cloned = {} as T
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      cloned[key] = deepClone(obj[key])
    }
  }
  
  return cloned
}

// 防抖函数
export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  wait: number
): (...args: Parameters<T>) => void => {
  let timeout: NodeJS.Timeout
  
  return (...args: Parameters<T>) => {
    clearTimeout(timeout)
    timeout = setTimeout(() => func.apply(this, args), wait)
  }
}

// 节流函数
export const throttle = <T extends (...args: any[]) => any>(
  func: T,
  wait: number
): (...args: Parameters<T>) => void => {
  let timeout: NodeJS.Timeout | null = null
  let previous = 0
  
  return (...args: Parameters<T>) => {
    const now = Date.now()
    const remaining = wait - (now - previous)
    
    if (remaining <= 0) {
      if (timeout) {
        clearTimeout(timeout)
        timeout = null
      }
      previous = now
      func.apply(this, args)
    } else if (!timeout) {
      timeout = setTimeout(() => {
        previous = Date.now()
        timeout = null
        func.apply(this, args)
      }, remaining)
    }
  }
}

// 成功消息
export const showSuccess = (message: string) => {
  ElMessage.success(message)
}

// 错误消息
export const showError = (message: string) => {
  ElMessage.error(message)
}

// 警告消息
export const showWarning = (message: string) => {
  ElMessage.warning(message)
}

// 确认对话框
export const confirm = async (
  message: string,
  title: string = '提示',
  confirmButtonText: string = '确定',
  cancelButtonText: string = '取消'
): Promise<boolean> => {
  try {
    await ElMessageBox.confirm(message, title, {
      confirmButtonText,
      cancelButtonText,
      type: 'warning'
    })
    return true
  } catch {
    return false
  }
}

// 生成随机颜色
export const generateRandomColor = (): string => {
  const colors = [
    '#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399',
    '#36A2EB', '#4BC0C0', '#FFCE56', '#FF6384', '#9966FF'
  ]
  return colors[Math.floor(Math.random() * colors.length)]
}

// 下载文件
export const downloadFile = (url: string, filename: string) => {
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

// 复制文本到剪贴板
export const copyToClipboard = async (text: string): Promise<boolean> => {
  try {
    await navigator.clipboard.writeText(text)
    showSuccess('复制成功')
    return true
  } catch (err) {
    // 降级方案
    const textArea = document.createElement('textarea')
    textArea.value = text
    document.body.appendChild(textArea)
    textArea.select()
    document.execCommand('copy')
    document.body.removeChild(textArea)
    showSuccess('复制成功')
    return true
  }
}

// 验证邮箱格式
export const validateEmail = (email: string): boolean => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return regex.test(email)
}

// 验证手机号格式
export const validatePhone = (phone: string): boolean => {
  const regex = /^1[3-9]\d{9}$/
  return regex.test(phone)
}

// 生成唯一ID
export const generateId = (): string => {
  return Date.now().toString(36) + Math.random().toString(36).substr(2)
}