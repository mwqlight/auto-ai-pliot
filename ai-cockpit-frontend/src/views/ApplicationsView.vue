<template>
  <div class="applications-view">
    <!-- 页面标题和搜索栏 -->
    <div class="page-header">
      <h1 class="page-title">业务应用市场</h1>
      <div class="header-actions">
        <div class="search-bar">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="搜索应用..."
            class="tech-input search-input"
          />
          <button class="search-btn">🔍</button>
        </div>
        <button class="tech-btn primary-btn" @click="showCreateModal">
          <span>新建应用</span>
        </button>
      </div>
    </div>
    
    <!-- 筛选器 -->
    <div class="filters-section">
      <div class="filter-group">
        <label>应用类型：</label>
        <select v-model="selectedType" class="tech-input filter-select">
          <option value="">全部</option>
          <option value="customer-service">智能客服</option>
          <option value="document-ai">文档智能</option>
          <option value="data-analysis">数据分析</option>
          <option value="automation">流程自动化</option>
        </select>
      </div>
      
      <div class="filter-group">
        <label>状态：</label>
        <select v-model="selectedStatus" class="tech-input filter-select">
          <option value="">全部</option>
          <option value="active">运行中</option>
          <option value="stopped">已停止</option>
          <option value="error">异常</option>
        </select>
      </div>
      
      <div class="filter-group">
        <label>排序：</label>
        <select v-model="sortBy" class="tech-input filter-select">
          <option value="name">名称</option>
          <option value="created">创建时间</option>
          <option value="updated">更新时间</option>
        </select>
      </div>
    </div>
    
    <!-- 应用网格 -->
    <div class="applications-grid tech-grid tech-grid-3">
      <div 
        v-for="app in filteredApplications" 
        :key="app.id"
        class="application-card tech-card"
        @click="viewApplication(app)"
      >
        <div class="app-header">
          <div class="app-icon">{{ app.icon }}</div>
          <div class="app-status" :class="app.status"></div>
        </div>
        
        <div class="app-content">
          <h3 class="app-name">{{ app.name }}</h3>
          <p class="app-description">{{ app.description }}</p>
          <div class="app-meta">
            <span class="app-type">{{ app.type }}</span>
            <span class="app-version">v{{ app.version }}</span>
          </div>
        </div>
        
        <div class="app-footer">
          <div class="app-stats">
            <div class="stat">
              <span class="stat-label">调用次数</span>
              <span class="stat-value">{{ app.calls }}</span>
            </div>
            <div class="stat">
              <span class="stat-label">成功率</span>
              <span class="stat-value">{{ app.successRate }}%</span>
            </div>
          </div>
          
          <div class="app-actions">
            <button 
              class="action-btn" 
              @click.stop="toggleAppStatus(app)"
              :class="{ active: app.status === 'active' }"
            >
              {{ app.status === 'active' ? '停止' : '启动' }}
            </button>
            <button class="action-btn" @click.stop="showAppDetails(app)">详情</button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-if="filteredApplications.length === 0" class="empty-state">
      <div class="empty-icon">📱</div>
      <h3>暂无应用</h3>
      <p>创建您的第一个业务应用</p>
      <button class="tech-btn" @click="showCreateModal">创建应用</button>
    </div>
    
    <!-- 分页 -->
    <div class="pagination" v-if="filteredApplications.length > 0">
      <button 
        class="pagination-btn" 
        :disabled="currentPage === 1"
        @click="currentPage--"
      >
        上一页
      </button>
      <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
      <button 
        class="pagination-btn" 
        :disabled="currentPage === totalPages"
        @click="currentPage++"
      >
        下一页
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

// 搜索和筛选状态
const searchQuery = ref('')
const selectedType = ref('')
const selectedStatus = ref('')
const sortBy = ref('name')
const currentPage = ref(1)
const itemsPerPage = ref(9)

// 模拟应用数据
const applications = ref([
  {
    id: 1,
    name: '智能客服助手',
    description: '基于AI的智能客服系统，支持多轮对话和情感分析',
    type: 'customer-service',
    icon: '🤖',
    status: 'active',
    version: '1.2.0',
    calls: 12543,
    successRate: 98.5,
    created: '2024-01-15',
    updated: '2024-03-20'
  },
  {
    id: 2,
    name: '文档智能分析',
    description: '自动提取和分析文档内容，支持多种格式',
    type: 'document-ai',
    icon: '📄',
    status: 'active',
    version: '2.1.0',
    calls: 8921,
    successRate: 95.2,
    created: '2024-02-10',
    updated: '2024-03-18'
  },
  {
    id: 3,
    name: '数据可视化平台',
    description: '实时数据分析和可视化展示',
    type: 'data-analysis',
    icon: '📊',
    status: 'stopped',
    version: '1.0.3',
    calls: 4567,
    successRate: 99.1,
    created: '2024-03-01',
    updated: '2024-03-15'
  },
  {
    id: 4,
    name: '流程自动化引擎',
    description: '自动化业务流程，提高工作效率',
    type: 'automation',
    icon: '⚙️',
    status: 'active',
    version: '1.5.0',
    calls: 6789,
    successRate: 97.8,
    created: '2024-01-25',
    updated: '2024-03-22'
  },
  {
    id: 5,
    name: '智能推荐系统',
    description: '个性化内容推荐引擎',
    type: 'data-analysis',
    icon: '🎯',
    status: 'error',
    version: '2.0.1',
    calls: 2345,
    successRate: 92.3,
    created: '2024-02-28',
    updated: '2024-03-19'
  },
  {
    id: 6,
    name: '语音识别服务',
    description: '高精度语音转文字服务',
    type: 'customer-service',
    icon: '🎤',
    status: 'active',
    version: '1.3.0',
    calls: 11234,
    successRate: 96.7,
    created: '2024-01-20',
    updated: '2024-03-21'
  }
])

// 计算属性
const filteredApplications = computed(() => {
  let filtered = applications.value
  
  // 搜索过滤
  if (searchQuery.value) {
    filtered = filtered.filter(app => 
      app.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      app.description.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }
  
  // 类型过滤
  if (selectedType.value) {
    filtered = filtered.filter(app => app.type === selectedType.value)
  }
  
  // 状态过滤
  if (selectedStatus.value) {
    filtered = filtered.filter(app => app.status === selectedStatus.value)
  }
  
  // 排序
  filtered.sort((a, b) => {
    if (sortBy.value === 'name') {
      return a.name.localeCompare(b.name)
    } else if (sortBy.value === 'created') {
      return new Date(b.created).getTime() - new Date(a.created).getTime()
    } else if (sortBy.value === 'updated') {
      return new Date(b.updated).getTime() - new Date(a.updated).getTime()
    }
    return 0
  })
  
  return filtered
})

const totalPages = computed(() => {
  return Math.ceil(filteredApplications.value.length / itemsPerPage.value)
})

// 方法
const viewApplication = (app: any) => {
  console.log('查看应用:', app)
  // 跳转到应用详情页面
}

const toggleAppStatus = (app: any) => {
  app.status = app.status === 'active' ? 'stopped' : 'active'
}

const showAppDetails = (app: any) => {
  console.log('显示应用详情:', app)
  // 显示应用详情模态框
}

const showCreateModal = () => {
  console.log('显示创建应用模态框')
  // 显示创建应用模态框
}
</script>

<style scoped>
.applications-view {
  min-height: 100vh;
  padding: 24px 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  background: linear-gradient(135deg, var(--tech-primary) 0%, var(--success) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-bar {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  overflow: hidden;
}

.search-input {
  border: none;
  background: transparent;
  width: 300px;
  padding: 12px 16px;
}

.search-btn {
  background: var(--tech-primary);
  border: none;
  color: white;
  padding: 12px 16px;
  cursor: pointer;
  transition: all var(--transition-normal);
}

.search-btn:hover {
  background: var(--tech-primary-light);
}

.primary-btn {
  background: linear-gradient(135deg, var(--tech-primary) 0%, var(--success) 100%);
}

.filters-section {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-group label {
  font-size: 14px;
  color: var(--text-secondary);
  white-space: nowrap;
}

.filter-select {
  min-width: 120px;
}

.applications-grid {
  margin-bottom: 32px;
}

.application-card {
  cursor: pointer;
  transition: all var(--transition-normal);
  display: flex;
  flex-direction: column;
  height: 280px;
}

.application-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--glow-tech);
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.app-icon {
  font-size: 32px;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 212, 255, 0.1);
  border-radius: 12px;
}

.app-status {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.app-status.active {
  background: var(--success);
  box-shadow: 0 0 10px var(--success);
}

.app-status.stopped {
  background: var(--text-muted);
}

.app-status.error {
  background: var(--error);
  box-shadow: 0 0 10px var(--error);
}

.app-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.app-name {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.app-description {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.4;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.app-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.app-type {
  font-size: 12px;
  color: var(--tech-primary);
  background: rgba(0, 212, 255, 0.1);
  padding: 4px 8px;
  border-radius: 4px;
}

.app-version {
  font-size: 12px;
  color: var(--text-muted);
}

.app-footer {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.app-stats {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.stat {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-label {
  font-size: 11px;
  color: var(--text-muted);
  margin-bottom: 2px;
}

.stat-value {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.app-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  flex: 1;
  padding: 6px 12px;
  border: 1px solid var(--border-tech);
  background: transparent;
  color: var(--text-secondary);
  border-radius: 4px;
  cursor: pointer;
  transition: all var(--transition-normal);
  font-size: 12px;
}

.action-btn:hover {
  background: var(--tech-primary);
  color: white;
  border-color: var(--tech-primary);
}

.action-btn.active {
  background: var(--error);
  color: white;
  border-color: var(--error);
}

.empty-state {
  text-align: center;
  padding: 80px 0;
  color: var(--text-muted);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-state h3 {
  font-size: 24px;
  margin-bottom: 8px;
  color: var(--text-primary);
}

.empty-state p {
  margin-bottom: 24px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 32px;
}

.pagination-btn {
  padding: 8px 16px;
  border: 1px solid var(--border-tech);
  background: transparent;
  color: var(--text-secondary);
  border-radius: 4px;
  cursor: pointer;
  transition: all var(--transition-normal);
}

.pagination-btn:hover:not(:disabled) {
  background: var(--tech-primary);
  color: white;
  border-color: var(--tech-primary);
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: var(--text-muted);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .page-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .header-actions {
    justify-content: space-between;
  }
  
  .search-input {
    width: 200px;
  }
  
  .filters-section {
    flex-wrap: wrap;
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .applications-grid {
    grid-template-columns: 1fr;
  }
  
  .search-input {
    width: 150px;
  }
  
  .filter-group {
    flex: 1;
    min-width: 120px;
  }
}
</style>