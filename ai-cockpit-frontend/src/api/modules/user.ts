import { request } from '@/api'
import type { UserInfo, PaginationParams, PaginationResponse } from '@/types'

// 用户登录
export const login = (data: { username: string; password: string }) => {
  return request.post('/auth/login', data)
}

// 用户注册
export const register = (data: {
  username: string
  password: string
  email: string
  phone: string
  realName: string
}) => {
  return request.post('/auth/register', data)
}

// 获取当前用户信息
export const getCurrentUser = () => {
  return request.get<UserInfo>('/auth/me')
}

// 刷新token
export const refreshToken = () => {
  return request.post('/auth/refresh')
}

// 用户登出
export const logout = () => {
  return request.post('/auth/logout')
}

// 获取用户列表
export const getUserList = (params: PaginationParams & { username?: string; status?: number }) => {
  return request.get<PaginationResponse<UserInfo>>('/users', { params })
}

// 根据ID获取用户信息
export const getUserById = (id: number) => {
  return request.get<UserInfo>(`/users/${id}`)
}

// 更新用户信息
export const updateUser = (id: number, data: Partial<UserInfo>) => {
  return request.put(`/users/${id}`, data)
}

// 删除用户
export const deleteUser = (id: number) => {
  return request.delete(`/users/${id}`)
}

// 修改密码
export const changePassword = (data: { oldPassword: string; newPassword: string }) => {
  return request.put('/auth/password', data)
}

// 重置密码
export const resetPassword = (id: number) => {
  return request.post(`/users/${id}/reset-password`)
}

// 启用/禁用用户
export const toggleUserStatus = (id: number) => {
  return request.put(`/users/${id}/status`)
}

// 分配角色
export const assignRoles = (id: number, roleIds: number[]) => {
  return request.put(`/users/${id}/roles`, { roleIds })
}