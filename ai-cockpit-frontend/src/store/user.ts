import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserInfo } from '@/types'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>('')
  const userInfo = ref<UserInfo | null>(null)
  const permissions = ref<string[]>([])
  const roles = ref<string[]>([])

  // 设置token
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // 设置用户信息
  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
  }

  // 设置权限
  const setPermissions = (perms: string[]) => {
    permissions.value = perms
  }

  // 设置角色
  const setRoles = (userRoles: string[]) => {
    roles.value = userRoles
  }

  // 登录
  const login = async (userData: { username: string; password: string }) => {
    // 这里会调用API进行登录
    // 暂时模拟登录成功
    const mockToken = 'mock-jwt-token'
    const mockUserInfo: UserInfo = {
      id: 1,
      username: userData.username,
      email: 'admin@aicockpit.com',
      phone: '13800138000',
      realName: '管理员',
      avatar: '',
      status: 1,
      createTime: new Date().toISOString(),
      updateTime: new Date().toISOString()
    }
    
    setToken(mockToken)
    setUserInfo(mockUserInfo)
    setPermissions(['user:read', 'user:write', 'model:read', 'model:write'])
    setRoles(['admin'])
    
    return Promise.resolve()
  }

  // 登出
  const logout = () => {
    token.value = ''
    userInfo.value = null
    permissions.value = []
    roles.value = []
    localStorage.removeItem('token')
  }

  // 检查是否已登录
  const isLoggedIn = () => {
    return !!token.value
  }

  // 检查是否有权限
  const hasPermission = (permission: string) => {
    return permissions.value.includes(permission)
  }

  // 检查是否有角色
  const hasRole = (role: string) => {
    return roles.value.includes(role)
  }

  // 初始化用户状态
  const initUserState = () => {
    const savedToken = localStorage.getItem('token')
    if (savedToken) {
      token.value = savedToken
      // 这里应该调用API获取用户信息
    }
  }

  return {
    token,
    userInfo,
    permissions,
    roles,
    setToken,
    setUserInfo,
    setPermissions,
    setRoles,
    login,
    logout,
    isLoggedIn,
    hasPermission,
    hasRole,
    initUserState
  }
})