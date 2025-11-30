<template>
  <div class="resource-monitor">
    <!-- 筛选工具栏 -->
    <div class="filter-toolbar">
      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="监控类型">
          <el-select v-model="filterForm.monitorType" placeholder="请选择监控类型" clearable>
            <el-option label="CPU使用率" value="cpu" />
            <el-option label="内存使用率" value="memory" />
            <el-option label="磁盘使用率" value="disk" />
            <el-option label="网络流量" value="network" />
            <el-option label="GPU使用率" value="gpu" />
            <el-option label="服务状态" value="service" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="目标类型">
          <el-select v-model="filterForm.targetType" placeholder="请选择目标类型" clearable>
            <el-option label="服务器" value="server" />
            <el-option label="容器" value="container" />
            <el-option label="服务" value="service" />
            <el-option label="数据库" value="database" />
            <el-option label="缓存" value="cache" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="0" />
            <el-option label="警告" :value="1" />
            <el-option label="严重" :value="2" />
            <el-option label="未知" :value="3" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :loading="loading">
            <i class="el-icon-search"></i>
            查询
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            重置
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="toolbar-actions">
        <el-button type="success" @click="handleAdd">
          <i class="el-icon-plus"></i>
          添加监控
        </el-button>
        <el-button type="warning" @click="handleRefresh">
          <i class="el-icon-refresh"></i>
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 监控概览卡片 -->
    <div class="overview-cards">
      <el-row :gutter="16">
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon cpu">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.cpu.usage }}%</div>
              <div class="card-label">CPU使用率</div>
              <div class="card-trend" :class="overview.cpu.trend">
                <i :class="overview.cpu.trendIcon"></i>
                {{ overview.cpu.change }}
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon memory">
              <i class="el-icon-data-board"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.memory.usage }}%</div>
              <div class="card-label">内存使用率</div>
              <div class="card-trend" :class="overview.memory.trend">
                <i :class="overview.memory.trendIcon"></i>
                {{ overview.memory.change }}
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon disk">
              <i class="el-icon-folder-opened"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.disk.usage }}%</div>
              <div class="card-label">磁盘使用率</div>
              <div class="card-trend" :class="overview.disk.trend">
                <i :class="overview.disk.trendIcon"></i>
                {{ overview.disk.change }}
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon network">
              <i class="el-icon-connection"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.network.speed }} MB/s</div>
              <div class="card-label">网络流量</div>
              <div class="card-trend" :class="overview.network.trend">
                <i :class="overview.network.trendIcon"></i>
                {{ overview.network.change }}
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 监控列表 -->
    <div class="monitor-table">
      <el-table
        :data="tableData"
        v-loading="loading"
        style="width: 100%"
        :row-class-name="getRowClassName"
      >
        <el-table-column prop="targetName" label="监控目标" min-width="150">
          <template #default="{ row }">
            <div class="target-info">
              <div class="target-name">{{ row.targetName }}</div>
              <div class="target-type">{{ getTargetTypeLabel(row.targetType) }}</div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="monitorType" label="监控类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getMonitorTypeTag(row.monitorType)">
              {{ getMonitorTypeLabel(row.monitorType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="metricValue" label="当前值" width="120">
          <template #default="{ row }">
            <div class="metric-value">
              <span :class="getValueClass(row)">{{ row.metricValue }}</span>
              <span class="metric-unit">{{ row.metricUnit }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)" effect="dark">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="monitorTimestamp" label="最后更新" width="180">
          <template #default="{ row }">
            {{ formatTime(row.monitorTimestamp) }}
          </template>
        </el-table-column>
        
        <el-table-column prop="availabilityPercentage" label="可用性" width="100">
          <template #default="{ row }">
            <div class="availability">
              <el-progress 
                :percentage="row.availabilityPercentage" 
                :show-text="false" 
                :stroke-width="8"
                :color="getAvailabilityColor(row.availabilityPercentage)"
              />
              <span class="availability-text">{{ row.availabilityPercentage }}%</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">
              详情
            </el-button>
            <el-button size="small" type="warning" @click="handleEdit(row)">
              配置
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 添加/编辑监控对话框 -->
    <MonitorDialog 
      v-model="dialogVisible"
      :data="currentData"
      :mode="dialogMode"
      @success="handleDialogSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import MonitorDialog from './components/MonitorDialog.vue'

// 类型定义
interface MonitorItem {
  id: number
  monitorType: string
  targetType: string
  targetId: number
  targetName: string
  metricValue: number
  metricUnit: string
  status: number
  monitorTimestamp: string
  availabilityPercentage: number
  warningThreshold?: number
  criticalThreshold?: number
}

interface OverviewData {
  cpu: { usage: number; trend: string; trendIcon: string; change: string }
  memory: { usage: number; trend: string; trendIcon: string; change: string }
  disk: { usage: number; trend: string; trendIcon: string; change: string }
  network: { speed: number; trend: string; trendIcon: string; change: string }
}

// 响应式数据
const loading = ref(false)
const tableData = ref<MonitorItem[]>([])
const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const currentData = ref<MonitorItem | null>(null)

const filterForm = reactive({
  monitorType: '',
  targetType: '',
  status: null as number | null
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const overview = reactive<OverviewData>({
  cpu: { usage: 0, trend: '', trendIcon: '', change: '' },
  memory: { usage: 0, trend: '', trendIcon: '', change: '' },
  disk: { usage: 0, trend: '', trendIcon: '', change: '' },
  network: { speed: 0, trend: '', trendIcon: '', change: '' }
})

// 模拟数据
const mockData: MonitorItem[] = [
  {
    id: 1,
    monitorType: 'cpu',
    targetType: 'server',
    targetId: 1,
    targetName: '主服务器-01',
    metricValue: 15.2,
    metricUnit: '%',
    status: 0,
    monitorTimestamp: new Date().toISOString(),
    availabilityPercentage: 99.8,
    warningThreshold: 80,
    criticalThreshold: 90
  },
  {
    id: 2,
    monitorType: 'memory',
    targetType: 'server',
    targetId: 1,
    targetName: '主服务器-01',
    metricValue: 45.6,
    metricUnit: '%',
    status: 0,
    monitorTimestamp: new Date().toISOString(),
    availabilityPercentage: 99.5,
    warningThreshold: 85,
    criticalThreshold: 95
  },
  {
    id: 3,
    monitorType: 'disk',
    targetType: 'server',
    targetId: 1,
    targetName: '主服务器-01',
    metricValue: 23.8,
    metricUnit: '%',
    status: 0,
    monitorTimestamp: new Date().toISOString(),
    availabilityPercentage: 98.9,
    warningThreshold: 85,
    criticalThreshold: 95
  },
  {
    id: 4,
    monitorType: 'service',
    targetType: 'service',
    targetId: 2,
    targetName: 'AI模型服务',
    metricValue: 100,
    metricUnit: '%',
    status: 0,
    monitorTimestamp: new Date().toISOString(),
    availabilityPercentage: 99.9,
    warningThreshold: 95,
    criticalThreshold: 98
  },
  {
    id: 5,
    monitorType: 'database',
    targetType: 'database',
    targetId: 3,
    targetName: 'MySQL数据库',
    metricValue: 12.3,
    metricUnit: '%',
    status: 0,
    monitorTimestamp: new Date().toISOString(),
    availabilityPercentage: 99.7,
    warningThreshold: 80,
    criticalThreshold: 90
  }
]

// 方法定义
const fetchData = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    tableData.value = mockData
    pagination.total = mockData.length
    
    // 更新概览数据
    updateOverviewData()
  } catch (error) {
    ElMessage.error('获取监控数据失败')
  } finally {
    loading.value = false
  }
}

const updateOverviewData = () => {
  // 模拟实时数据更新
  overview.cpu = {
    usage: Math.random() * 100,
    trend: Math.random() > 0.5 ? 'up' : 'down',
    trendIcon: Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom',
    change: Math.random() > 0.5 ? '+2.3%' : '-1.7%'
  }
  
  overview.memory = {
    usage: Math.random() * 100,
    trend: Math.random() > 0.5 ? 'up' : 'down',
    trendIcon: Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom',
    change: Math.random() > 0.5 ? '+1.8%' : '-0.9%'
  }
  
  overview.disk = {
    usage: Math.random() * 100,
    trend: Math.random() > 0.5 ? 'up' : 'down',
    trendIcon: Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom',
    change: Math.random() > 0.5 ? '+0.5%' : '-0.3%'
  }
  
  overview.network = {
    speed: Math.random() * 100,
    trend: Math.random() > 0.5 ? 'up' : 'down',
    trendIcon: Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom',
    change: Math.random() > 0.5 ? '+5.2MB/s' : '-2.1MB/s'
  }
}

const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(filterForm, {
    monitorType: '',
    targetType: '',
    status: null
  })
  pagination.current = 1
  fetchData()
}

const handleRefresh = () => {
  fetchData()
}

const handleAdd = () => {
  dialogMode.value = 'add'
  currentData.value = null
  dialogVisible.value = true
}

const handleEdit = (row: MonitorItem) => {
  dialogMode.value = 'edit'
  currentData.value = { ...row }
  dialogVisible.value = true
}

const handleView = (row: MonitorItem) => {
  // 跳转到详情页面
  console.log('查看详情:', row)
}

const handleDialogSuccess = () => {
  dialogVisible.value = false
  fetchData()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  fetchData()
}

const handleCurrentChange = (page: number) => {
  pagination.current = page
  fetchData()
}

// 工具函数
const getMonitorTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    'cpu': 'CPU使用率',
    'memory': '内存使用率',
    'disk': '磁盘使用率',
    'network': '网络流量',
    'gpu': 'GPU使用率',
    'service': '服务状态'
  }
  return map[type] || type
}

const getMonitorTypeTag = (type: string) => {
  const map: Record<string, string> = {
    'cpu': 'primary',
    'memory': 'success',
    'disk': 'warning',
    'network': 'info',
    'gpu': 'danger',
    'service': ''
  }
  return map[type] || 'info'
}

const getTargetTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    'server': '服务器',
    'container': '容器',
    'service': '服务',
    'database': '数据库',
    'cache': '缓存'
  }
  return map[type] || type
}

const getStatusLabel = (status: number) => {
  const map: Record<number, string> = {
    0: '正常',
    1: '警告',
    2: '严重',
    3: '未知'
  }
  return map[status] || '未知'
}

const getStatusTag = (status: number) => {
  const map: Record<number, string> = {
    0: 'success',
    1: 'warning',
    2: 'danger',
    3: 'info'
  }
  return map[status] || 'info'
}

const getValueClass = (row: MonitorItem) => {
  if (row.status === 0) return 'value-normal'
  if (row.status === 1) return 'value-warning'
  if (row.status === 2) return 'value-critical'
  return 'value-unknown'
}

const getRowClassName = ({ row }: { row: MonitorItem }) => {
  if (row.status === 1) return 'warning-row'
  if (row.status === 2) return 'critical-row'
  return ''
}

const getAvailabilityColor = (percentage: number) => {
  if (percentage >= 99) return '#67c23a'
  if (percentage >= 95) return '#e6a23c'
  return '#f56c6c'
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.resource-monitor {
  padding: 0;
}

.filter-toolbar {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 16px;
}

.filter-form {
  margin-bottom: 0;
}

.filter-form :deep(.el-form-item) {
  margin-bottom: 0;
}

.filter-form :deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.7);
}

.toolbar-actions {
  display: flex;
  gap: 12px;
}

.overview-cards {
  margin-bottom: 20px;
}

.overview-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s ease;
}

.overview-card:hover {
  background: rgba(255, 255, 255, 0.08);
  transform: translateY(-2px);
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
}

.card-icon.cpu {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.card-icon.memory {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.card-icon.disk {
  background: linear-gradient(135deg, #e6a23c, #ebb563);
}

.card-icon.network {
  background: linear-gradient(135deg, #909399, #a6a9ad);
}

.card-content {
  flex: 1;
}

.card-value {
  font-size: 24px;
  font-weight: 700;
  color: #fff;
  line-height: 1;
  margin-bottom: 4px;
}

.card-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 4px;
}

.card-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.card-trend.up {
  color: #f56c6c;
}

.card-trend.down {
  color: #67c23a;
}

.monitor-table {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 8px;
  overflow: hidden;
}

.target-info {
  line-height: 1.4;
}

.target-name {
  color: #fff;
  font-weight: 500;
}

.target-type {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

.metric-value {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.value-normal {
  color: #67c23a;
  font-weight: 600;
}

.value-warning {
  color: #e6a23c;
  font-weight: 600;
}

.value-critical {
  color: #f56c6c;
  font-weight: 600;
}

.value-unknown {
  color: #909399;
  font-weight: 600;
}

.metric-unit {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

.availability {
  display: flex;
  align-items: center;
  gap: 8px;
}

.availability-text {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
  min-width: 40px;
}

.pagination-container {
  padding: 20px;
  display: flex;
  justify-content: center;
}

/* 表格行样式 */
:deep(.warning-row) {
  background: rgba(230, 162, 60, 0.1) !important;
}

:deep(.critical-row) {
  background: rgba(245, 108, 108, 0.1) !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .filter-toolbar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .toolbar-actions {
    justify-content: center;
  }
  
  .overview-card {
    padding: 16px;
  }
  
  .card-icon {
    width: 50px;
    height: 50px;
    font-size: 24px;
  }
  
  .card-value {
    font-size: 20px;
  }
}
</style>