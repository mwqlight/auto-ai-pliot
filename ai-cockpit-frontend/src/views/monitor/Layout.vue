<template>
  <div class="monitor-layout">
    <!-- 监控中心头部 -->
    <div class="monitor-header">
      <div class="header-content">
        <h1 class="title">
          <i class="el-icon-monitor"></i>
          资源监控中心
        </h1>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">监控项</div>
          </div>
          <div class="stat-item">
            <div class="stat-value normal">{{ stats.normal }}</div>
            <div class="stat-label">正常</div>
          </div>
          <div class="stat-item">
            <div class="stat-value warning">{{ stats.warning }}</div>
            <div class="stat-label">警告</div>
          </div>
          <div class="stat-item">
            <div class="stat-value critical">{{ stats.critical }}</div>
            <div class="stat-label">严重</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 监控中心导航 -->
    <div class="monitor-nav">
      <el-menu
        :default-active="activeTab"
        mode="horizontal"
        @select="handleTabSelect"
        class="monitor-menu"
      >
        <el-menu-item index="resources">
          <i class="el-icon-cpu"></i>
          资源监控
        </el-menu-item>
        <el-menu-item index="performance">
          <i class="el-icon-data-line"></i>
          性能监控
        </el-menu-item>
        <el-menu-item index="api">
          <i class="el-icon-connection"></i>
          API监控
        </el-menu-item>
        <el-menu-item index="alerts">
          <i class="el-icon-bell"></i>
          告警管理
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 监控内容区域 -->
    <div class="monitor-content">
      <router-view />
    </div>

    <!-- 实时数据更新提示 -->
    <div v-if="isRealTime" class="real-time-indicator">
      <span class="indicator-dot"></span>
      实时更新中
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 响应式数据
const activeTab = ref('resources')
const isRealTime = ref(true)
const stats = ref({
  total: 0,
  normal: 0,
  warning: 0,
  critical: 0
})

// 定时器
let statsTimer: NodeJS.Timeout

// 监听路由变化
const updateActiveTab = () => {
  const path = route.path
  if (path.includes('/monitor/resources')) {
    activeTab.value = 'resources'
  } else if (path.includes('/monitor/performance')) {
    activeTab.value = 'performance'
  } else if (path.includes('/monitor/api')) {
    activeTab.value = 'api'
  } else if (path.includes('/monitor/alerts')) {
    activeTab.value = 'alerts'
  }
}

// 标签页切换
const handleTabSelect = (key: string) => {
  activeTab.value = key
  router.push(`/monitor/${key}`)
}

// 获取统计信息
const fetchStats = async () => {
  try {
    // 模拟数据
    stats.value = {
      total: 156,
      normal: 142,
      warning: 10,
      critical: 4
    }
  } catch (error) {
    console.error('获取监控统计失败:', error)
  }
}

// 实时更新统计信息
const startRealTimeUpdate = () => {
  statsTimer = setInterval(fetchStats, 30000) // 30秒更新一次
}

// 停止实时更新
const stopRealTimeUpdate = () => {
  if (statsTimer) {
    clearInterval(statsTimer)
  }
}

onMounted(() => {
  updateActiveTab()
  fetchStats()
  startRealTimeUpdate()
})

onUnmounted(() => {
  stopRealTimeUpdate()
})
</script>

<style scoped>
.monitor-layout {
  min-height: 100vh;
  background: linear-gradient(135deg, #0c0c0c 0%, #1a1a1a 100%);
  position: relative;
}

.monitor-header {
  background: rgba(255, 255, 255, 0.02);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  color: #fff;
  font-size: 24px;
  font-weight: 600;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.title i {
  color: #409eff;
  font-size: 28px;
}

.header-stats {
  display: flex;
  gap: 32px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #fff;
  line-height: 1;
}

.stat-value.normal {
  color: #67c23a;
}

.stat-value.warning {
  color: #e6a23c;
}

.stat-value.critical {
  color: #f56c6c;
}

.stat-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 4px;
}

.monitor-nav {
  background: rgba(255, 255, 255, 0.02);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.monitor-menu {
  max-width: 1200px;
  margin: 0 auto;
  background: transparent;
  border: none;
}

.monitor-menu .el-menu-item {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  border: none;
  background: transparent;
}

.monitor-menu .el-menu-item:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.05);
}

.monitor-menu .el-menu-item.is-active {
  color: #409eff;
  background: rgba(64, 158, 255, 0.1);
  border-bottom: 2px solid #409eff;
}

.monitor-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  min-height: calc(100vh - 200px);
}

.real-time-indicator {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background: rgba(64, 158, 255, 0.1);
  border: 1px solid rgba(64, 158, 255, 0.3);
  color: #409eff;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
  backdrop-filter: blur(10px);
}

.indicator-dot {
  width: 8px;
  height: 8px;
  background: #409eff;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.5;
    transform: scale(1.2);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }
  
  .header-stats {
    gap: 16px;
  }
  
  .stat-value {
    font-size: 24px;
  }
  
  .monitor-content {
    padding: 16px;
  }
}
</style>