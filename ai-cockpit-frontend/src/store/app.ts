import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  const sidebarOpened = ref<boolean>(true)
  const theme = ref<string>('dark')
  const loading = ref<boolean>(false)
  const globalSettings = ref<{
    language: string
    theme: string
    sidebarWidth: number
    navbarHeight: number
  }>({
    language: 'zh-CN',
    theme: 'dark',
    sidebarWidth: 240,
    navbarHeight: 60
  })

  // 切换侧边栏状态
  const toggleSidebar = () => {
    sidebarOpened.value = !sidebarOpened.value
  }

  // 设置侧边栏状态
  const setSidebarOpened = (opened: boolean) => {
    sidebarOpened.value = opened
  }

  // 切换主题
  const toggleTheme = () => {
    theme.value = theme.value === 'dark' ? 'light' : 'dark'
    globalSettings.value.theme = theme.value
  }

  // 设置主题
  const setTheme = (newTheme: string) => {
    theme.value = newTheme
    globalSettings.value.theme = newTheme
  }

  // 设置加载状态
  const setLoading = (isLoading: boolean) => {
    loading.value = isLoading
  }

  // 设置全局配置
  const setGlobalSettings = (settings: Partial<typeof globalSettings.value>) => {
    globalSettings.value = { ...globalSettings.value, ...settings }
  }

  return {
    sidebarOpened,
    theme,
    loading,
    globalSettings,
    toggleSidebar,
    setSidebarOpened,
    toggleTheme,
    setTheme,
    setLoading,
    setGlobalSettings
  }
})