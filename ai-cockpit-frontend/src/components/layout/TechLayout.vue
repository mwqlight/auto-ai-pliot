<template>
  <div class="tech-layout">
    <!-- 顶部导航栏 -->
    <header class="tech-header">
      <div class="header-left">
        <div class="logo">
          <div class="logo-icon">
            <div class="logo-glow"></div>
            <span>AI</span>
          </div>
          <span class="logo-text">AI Cockpit</span>
        </div>
      </div>
      
      <nav class="nav-menu">
        <router-link 
          v-for="item in menuItems" 
          :key="item.path" 
          :to="item.path"
          class="nav-item"
          :class="{ active: $route.path === item.path }"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span class="nav-text">{{ item.name }}</span>
        </router-link>
      </nav>
      
      <div class="header-right">
        <div class="user-info">
          <div class="user-avatar">
            <span>{{ userInitials }}</span>
          </div>
          <div class="user-details">
            <span class="user-name">{{ userName }}</span>
            <span class="user-role">{{ userRole }}</span>
          </div>
        </div>
        
        <div class="header-actions">
          <button class="tech-btn action-btn" @click="toggleTheme">
            <span class="action-icon">{{ themeIcon }}</span>
          </button>
          <button class="tech-btn action-btn" @click="showNotifications">
            <span class="action-icon">🔔</span>
            <span class="notification-badge" v-if="unreadCount > 0">{{ unreadCount }}</span>
          </button>
        </div>
      </div>
    </header>
    
    <!-- 主内容区域 -->
    <main class="tech-main">
      <div class="main-content">
        <router-view />
      </div>
    </main>
    
    <!-- 背景特效 -->
    <div class="tech-bg-effects">
      <div class="grid-lines"></div>
      <div class="floating-particles"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 主题状态
const isDarkTheme = ref(true)

// 用户信息
const userName = ref('管理员')
const userRole = ref('超级管理员')
const unreadCount = ref(3)

// 菜单项
const menuItems = ref([
  { name: '仪表盘', path: '/dashboard', icon: '📊' },
  { name: '业务应用', path: '/applications', icon: '🚀' },
  { name: '数据集', path: '/datasets', icon: '📁' },
  { name: '模型管理', path: '/models', icon: '🧠' },
  { name: '资源监控', path: '/monitoring', icon: '📈' },
  { name: '权限管理', path: '/permissions', icon: '🔐' },
  { name: '系统设置', path: '/settings', icon: '⚙️' }
])

// 计算属性
const userInitials = computed(() => {
  return userName.value.substring(0, 2).toUpperCase()
})

const themeIcon = computed(() => {
  return isDarkTheme.value ? '🌙' : '☀️'
})

// 方法
const toggleTheme = () => {
  isDarkTheme.value = !isDarkTheme.value
  document.documentElement.setAttribute('data-theme', isDarkTheme.value ? 'dark' : 'light')
}

const showNotifications = () => {
  // 通知功能实现
  console.log('显示通知')
}

onMounted(() => {
  // 初始化主题
  document.documentElement.setAttribute('data-theme', 'dark')
})
</script>

<style scoped>
.tech-layout {
  min-height: 100vh;
  background: var(--bg-gradient);
  position: relative;
  overflow: hidden;
}

.tech-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  height: 70px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid var(--border-tech);
  position: relative;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
}

.logo-icon {
  position: relative;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, var(--tech-primary) 0%, var(--tech-primary-dark) 100%);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
}

.logo-glow {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: var(--tech-primary);
  border-radius: 10px;
  filter: blur(8px);
  opacity: 0.3;
  z-index: -1;
}

.logo-text {
  background: linear-gradient(135deg, var(--tech-primary) 0%, var(--success) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav-menu {
  display: flex;
  gap: 8px;
  flex: 1;
  justify-content: center;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  border-radius: 8px;
  color: var(--text-secondary);
  text-decoration: none;
  transition: all var(--transition-normal);
  position: relative;
}

.nav-item:hover {
  background: rgba(0, 212, 255, 0.1);
  color: var(--text-primary);
}

.nav-item.active {
  background: rgba(0, 212, 255, 0.2);
  color: var(--tech-primary);
  box-shadow: var(--shadow-tech);
}

.nav-item.active::before {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 20%;
  right: 20%;
  height: 2px;
  background: var(--tech-primary);
  border-radius: 2px;
}

.nav-icon {
  font-size: 18px;
}

.nav-text {
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.05);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.1);
}

.user-avatar {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, var(--tech-primary) 0%, var(--success) 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 14px;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-name {
  font-weight: 600;
  font-size: 14px;
}

.user-role {
  font-size: 12px;
  color: var(--text-muted);
}

.header-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  width: 40px;
  height: 40px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
}

.action-icon {
  font-size: 18px;
}

.notification-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background: var(--error);
  color: white;
  border-radius: 50%;
  width: 16px;
  height: 16px;
  font-size: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tech-main {
  padding: 24px 32px;
  min-height: calc(100vh - 70px);
  position: relative;
  z-index: 10;
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
}

/* 背景特效 */
.tech-bg-effects {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 1;
}

.grid-lines {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    linear-gradient(rgba(0, 212, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 212, 255, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: grid-move 20s linear infinite;
}

@keyframes grid-move {
  0% { transform: translate(0, 0); }
  100% { transform: translate(50px, 50px); }
}

.floating-particles {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 20% 80%, rgba(0, 212, 255, 0.1) 0%, transparent 50%),
              radial-gradient(circle at 80% 20%, rgba(0, 255, 157, 0.1) 0%, transparent 50%);
  animation: float 15s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(-10px, -10px) scale(1.05); }
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .tech-header {
    padding: 0 16px;
  }
  
  .nav-text {
    display: none;
  }
  
  .nav-item {
    padding: 12px;
  }
  
  .user-details {
    display: none;
  }
}

@media (max-width: 768px) {
  .tech-main {
    padding: 16px;
  }
  
  .nav-menu {
    gap: 4px;
  }
  
  .nav-item {
    padding: 8px 12px;
  }
}
</style>