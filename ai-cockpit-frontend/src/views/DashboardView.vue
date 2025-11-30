<template>
  <div class="dashboard-view">
    <!-- 页面标题和统计卡片 -->
    <div class="dashboard-header">
      <h1 class="page-title">AI 驾驶舱</h1>
      <div class="stats-grid">
        <div class="stat-card tech-card">
          <div class="stat-icon">🚀</div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.applications }}</div>
            <div class="stat-label">业务应用</div>
          </div>
          <div class="stat-trend positive">+12%</div>
        </div>
        
        <div class="stat-card tech-card">
          <div class="stat-icon">📁</div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.datasets }}</div>
            <div class="stat-label">数据集</div>
          </div>
          <div class="stat-trend positive">+8%</div>
        </div>
        
        <div class="stat-card tech-card">
          <div class="stat-icon">🧠</div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.models }}</div>
            <div class="stat-label">AI 模型</div>
          </div>
          <div class="stat-trend positive">+15%</div>
        </div>
        
        <div class="stat-card tech-card">
          <div class="stat-icon">📈</div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.monitoring }}</div>
            <div class="stat-label">监控指标</div>
          </div>
          <div class="stat-trend negative">-3%</div>
        </div>
      </div>
    </div>
    
    <!-- 主要内容区域 -->
    <div class="dashboard-content">
      <!-- 左侧：图表和监控 -->
      <div class="content-left">
        <!-- 性能图表 -->
        <div class="chart-section tech-card">
          <div class="section-header">
            <h3>系统性能监控</h3>
            <div class="time-filter">
              <button 
                v-for="period in timePeriods" 
                :key="period.value"
                :class="{ active: activePeriod === period.value }"
                @click="activePeriod = period.value"
                class="filter-btn"
              >
                {{ period.label }}
              </button>
            </div>
          </div>
          <div class="chart-container">
            <div class="chart-placeholder">
              <div class="chart-glow"></div>
              <span>性能图表</span>
            </div>
          </div>
        </div>
        
        <!-- 实时监控 -->
        <div class="monitoring-section tech-card">
          <div class="section-header">
            <h3>实时资源监控</h3>
            <div class="status-indicator">
              <span class="status-dot active"></span>
              <span>运行中</span>
            </div>
          </div>
          <div class="monitoring-grid">
            <div class="monitor-item">
              <div class="monitor-label">CPU 使用率</div>
              <div class="monitor-value">{{ monitoring.cpu }}%</div>
              <div class="monitor-bar">
                <div 
                  class="monitor-progress" 
                  :style="{ width: monitoring.cpu + '%' }"
                  :class="{ warning: monitoring.cpu > 80, danger: monitoring.cpu > 90 }"
                ></div>
              </div>
            </div>
            
            <div class="monitor-item">
              <div class="monitor-label">内存使用率</div>
              <div class="monitor-value">{{ monitoring.memory }}%</div>
              <div class="monitor-bar">
                <div 
                  class="monitor-progress" 
                  :style="{ width: monitoring.memory + '%' }"
                  :class="{ warning: monitoring.memory > 80, danger: monitoring.memory > 90 }"
                ></div>
              </div>
            </div>
            
            <div class="monitor-item">
              <div class="monitor-label">磁盘使用率</div>
              <div class="monitor-value">{{ monitoring.disk }}%</div>
              <div class="monitor-bar">
                <div 
                  class="monitor-progress" 
                  :style="{ width: monitoring.disk + '%' }"
                  :class="{ warning: monitoring.disk > 80, danger: monitoring.disk > 90 }"
                ></div>
              </div>
            </div>
            
            <div class="monitor-item">
              <div class="monitor-label">网络流量</div>
              <div class="monitor-value">{{ monitoring.network }} MB/s</div>
              <div class="monitor-bar">
                <div 
                  class="monitor-progress" 
                  :style="{ width: Math.min(monitoring.network / 10, 100) + '%' }"
                  :class="{ warning: monitoring.network > 8, danger: monitoring.network > 9 }"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧：快速操作和通知 -->
      <div class="content-right">
        <!-- 快速操作 -->
        <div class="quick-actions tech-card">
          <div class="section-header">
            <h3>快速操作</h3>
          </div>
          <div class="actions-grid">
            <button 
              v-for="action in quickActions" 
              :key="action.id"
              class="action-btn tech-btn"
              @click="handleAction(action)"
            >
              <span class="action-icon">{{ action.icon }}</span>
              <span class="action-text">{{ action.label }}</span>
            </button>
          </div>
        </div>
        
        <!-- 最近活动 -->
        <div class="recent-activity tech-card">
          <div class="section-header">
            <h3>最近活动</h3>
            <router-link to="/activities" class="view-all">查看全部</router-link>
          </div>
          <div class="activity-list">
            <div 
              v-for="activity in recentActivities" 
              :key="activity.id"
              class="activity-item"
            >
              <div class="activity-icon">{{ activity.icon }}</div>
              <div class="activity-content">
                <div class="activity-title">{{ activity.title }}</div>
                <div class="activity-time">{{ activity.time }}</div>
              </div>
              <div class="activity-status" :class="activity.status">{{ activity.statusText }}</div>
            </div>
          </div>
        </div>
        
        <!-- 系统状态 -->
        <div class="system-status tech-card">
          <div class="section-header">
            <h3>系统状态</h3>
          </div>
          <div class="status-list">
            <div class="status-item">
              <span class="status-label">数据库连接</span>
              <span class="status-value online">正常</span>
            </div>
            <div class="status-item">
              <span class="status-label">缓存服务</span>
              <span class="status-value online">正常</span>
            </div>
            <div class="status-item">
              <span class="status-label">AI 服务</span>
              <span class="status-value online">正常</span>
            </div>
            <div class="status-item">
              <span class="status-label">文件存储</span>
              <span class="status-value warning">警告</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

// 统计数据
const stats = ref({
  applications: 24,
  datasets: 156,
  models: 42,
  monitoring: 18
})

// 监控数据
const monitoring = ref({
  cpu: 45,
  memory: 68,
  disk: 82,
  network: 3.2
})

// 时间周期
const activePeriod = ref('day')
const timePeriods = ref([
  { label: '今日', value: 'day' },
  { label: '本周', value: 'week' },
  { label: '本月', value: 'month' },
  { label: '本年', value: 'year' }
])

// 快速操作
const quickActions = ref([
  { id: 1, label: '新建应用', icon: '➕', action: 'createApp' },
  { id: 2, label: '上传数据', icon: '📤', action: 'uploadData' },
  { id: 3, label: '训练模型', icon: '⚡', action: 'trainModel' },
  { id: 4, label: '系统监控', icon: '👁️', action: 'monitorSystem' },
  { id: 5, label: '权限管理', icon: '🔐', action: 'managePermissions' },
  { id: 6, label: '数据备份', icon: '💾', action: 'backupData' }
])

// 最近活动
const recentActivities = ref([
  { id: 1, icon: '🚀', title: '新建业务应用 "智能客服"', time: '2分钟前', status: 'success', statusText: '完成' },
  { id: 2, icon: '📁', title: '数据集 "用户行为数据" 已更新', time: '5分钟前', status: 'success', statusText: '完成' },
  { id: 3, icon: '🧠', title: '模型训练任务开始执行', time: '10分钟前', status: 'processing', statusText: '进行中' },
  { id: 4, icon: '📈', title: '系统性能监控告警', time: '15分钟前', status: 'warning', statusText: '警告' },
  { id: 5, icon: '🔐', title: '权限配置已更新', time: '30分钟前', status: 'success', statusText: '完成' }
])

// 方法
const handleAction = (action: any) => {
  console.log('执行操作:', action)
  // 根据action.type执行相应操作
}

// 模拟实时数据更新
onMounted(() => {
  setInterval(() => {
    // 随机更新监控数据
    monitoring.value.cpu = Math.max(10, Math.min(95, monitoring.value.cpu + (Math.random() - 0.5) * 10))
    monitoring.value.memory = Math.max(15, Math.min(90, monitoring.value.memory + (Math.random() - 0.5) * 5))
    monitoring.value.network = Math.max(0.5, Math.min(9.5, monitoring.value.network + (Math.random() - 0.5) * 0.5))
  }, 5000)
})
</script>

<style scoped>
.dashboard-view {
  min-height: 100vh;
}

.dashboard-header {
  margin-bottom: 32px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  background: linear-gradient(135deg, var(--tech-primary) 0%, var(--success) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 24px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px;
}

.stat-icon {
  font-size: 32px;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 212, 255, 0.1);
  border-radius: 12px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: var(--text-secondary);
  margin-top: 4px;
}

.stat-trend {
  font-size: 14px;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: 4px;
}

.stat-trend.positive {
  background: rgba(0, 255, 157, 0.2);
  color: var(--success);
}

.stat-trend.negative {
  background: rgba(255, 77, 77, 0.2);
  color: var(--error);
}

.dashboard-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 32px;
}

.content-left {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.content-right {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.time-filter {
  display: flex;
  gap: 8px;
}

.filter-btn {
  padding: 6px 12px;
  border: 1px solid var(--border-tech);
  background: transparent;
  color: var(--text-secondary);
  border-radius: 6px;
  cursor: pointer;
  transition: all var(--transition-normal);
  font-size: 12px;
}

.filter-btn.active,
.filter-btn:hover {
  background: var(--tech-primary);
  color: white;
  border-color: var(--tech-primary);
}

.chart-container {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 8px;
  position: relative;
  overflow: hidden;
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  color: var(--text-muted);
  font-size: 16px;
}

.chart-glow {
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, var(--tech-primary) 0%, transparent 70%);
  border-radius: 50%;
  opacity: 0.3;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.3; }
  50% { transform: scale(1.1); opacity: 0.5; }
}

.monitoring-grid {
  display: grid;
  gap: 16px;
}

.monitor-item {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 12px;
  align-items: center;
}

.monitor-label {
  font-size: 14px;
  color: var(--text-secondary);
}

.monitor-value {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.monitor-bar {
  grid-column: 1 / -1;
  height: 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
  overflow: hidden;
}

.monitor-progress {
  height: 100%;
  background: linear-gradient(90deg, var(--tech-primary) 0%, var(--success) 100%);
  border-radius: 3px;
  transition: width 0.5s ease;
}

.monitor-progress.warning {
  background: linear-gradient(90deg, var(--warning) 0%, #ff9900 100%);
}

.monitor-progress.danger {
  background: linear-gradient(90deg, var(--error) 0%, #ff3333 100%);
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  height: auto;
}

.action-icon {
  font-size: 20px;
}

.action-text {
  font-size: 12px;
  font-weight: 500;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
  transition: all var(--transition-normal);
}

.activity-item:hover {
  background: rgba(255, 255, 255, 0.05);
}

.activity-icon {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 212, 255, 0.1);
  border-radius: 6px;
  font-size: 16px;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  color: var(--text-primary);
  margin-bottom: 2px;
}

.activity-time {
  font-size: 12px;
  color: var(--text-muted);
}

.activity-status {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  font-weight: 500;
}

.activity-status.success {
  background: rgba(0, 255, 157, 0.2);
  color: var(--success);
}

.activity-status.processing {
  background: rgba(0, 212, 255, 0.2);
  color: var(--tech-primary);
}

.activity-status.warning {
  background: rgba(255, 204, 0, 0.2);
  color: var(--warning);
}

.status-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.status-item:last-child {
  border-bottom: none;
}

.status-label {
  font-size: 14px;
  color: var(--text-secondary);
}

.status-value {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  font-weight: 500;
}

.status-value.online {
  background: rgba(0, 255, 157, 0.2);
  color: var(--success);
}

.status-value.warning {
  background: rgba(255, 204, 0, 0.2);
  color: var(--warning);
}

.view-all {
  font-size: 12px;
  color: var(--tech-primary);
  text-decoration: none;
  transition: all var(--transition-normal);
}

.view-all:hover {
  color: var(--tech-primary-light);
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: var(--text-muted);
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--success);
}

.status-dot.active {
  animation: blink 2s infinite;
}

@keyframes blink {
  0%, 50% { opacity: 1; }
  51%, 100% { opacity: 0.3; }
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .dashboard-content {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .actions-grid {
    grid-template-columns: 1fr;
  }
  
  .page-title {
    font-size: 24px;
  }
}
</style>