<template>
  <div class="monitor-detail">
    <!-- 页面头部 -->
    <div class="detail-header">
      <div class="header-left">
        <el-button icon="el-icon-arrow-left" @click="handleBack">
          返回
        </el-button>
        <h2 class="header-title">{{ monitorData.targetName }}</h2>
        <el-tag :type="getStatusTag(monitorData.status)" effect="dark">
          {{ getStatusLabel(monitorData.status) }}
        </el-tag>
      </div>
      <div class="header-actions">
        <el-button type="warning" @click="handleEdit">
          <i class="el-icon-edit"></i>
          编辑配置
        </el-button>
        <el-button 
          :type="monitorData.enabled ? 'danger' : 'success'" 
          @click="handleToggleStatus"
        >
          <i :class="monitorData.enabled ? 'el-icon-close' : 'el-icon-check'"></i>
          {{ monitorData.enabled ? '禁用' : '启用' }}
        </el-button>
        <el-button type="primary" @click="handleRefresh">
          <i class="el-icon-refresh"></i>
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 基本信息卡片 -->
    <el-row :gutter="16" class="info-cards">
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="info-card">
          <div class="card-icon">
            <i class="el-icon-monitor"></i>
          </div>
          <div class="card-content">
            <div class="card-label">监控类型</div>
            <div class="card-value">{{ getMonitorTypeLabel(monitorData.monitorType) }}</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="info-card">
          <div class="card-icon">
            <i class="el-icon-odometer"></i>
          </div>
          <div class="card-content">
            <div class="card-label">当前值</div>
            <div class="card-value" :class="getValueClass(monitorData)">
              {{ monitorData.metricValue }}{{ monitorData.metricUnit }}
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="info-card">
          <div class="card-icon">
            <i class="el-icon-time"></i>
          </div>
          <div class="card-content">
            <div class="card-label">最后更新</div>
            <div class="card-value">{{ formatTime(monitorData.monitorTimestamp) }}</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="info-card">
          <div class="card-icon">
            <i class="el-icon-success"></i>
          </div>
          <div class="card-content">
            <div class="card-label">可用性</div>
            <div class="card-value">
              <el-progress 
                :percentage="monitorData.availabilityPercentage" 
                :show-text="false" 
                :stroke-width="8"
                :color="getAvailabilityColor(monitorData.availabilityPercentage)"
              />
              <span class="availability-text">{{ monitorData.availabilityPercentage }}%</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <div class="chart-section">
      <el-row :gutter="16">
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>指标趋势</h3>
              <div class="chart-actions">
                <el-select v-model="timeRange" size="small" @change="handleTimeRangeChange">
                  <el-option label="1小时" value="1h" />
                  <el-option label="6小时" value="6h" />
                  <el-option label="24小时" value="24h" />
                  <el-option label="7天" value="7d" />
                </el-select>
              </div>
            </div>
            <div class="chart-container">
              <div ref="trendChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>状态分布</h3>
            </div>
            <div class="chart-container">
              <div ref="statusChart" class="chart"></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 配置信息 -->
    <div class="config-section">
      <el-card class="config-card">
        <template #header>
          <div class="card-header">
            <span>监控配置</span>
          </div>
        </template>
        
        <el-descriptions :column="2" border>
          <el-descriptions-item label="监控目标">
            {{ monitorData.targetName }}
          </el-descriptions-item>
          <el-descriptions-item label="目标类型">
            {{ getTargetTypeLabel(monitorData.targetType) }}
          </el-descriptions-item>
          <el-descriptions-item label="监控间隔">
            {{ monitorData.monitorInterval }} 秒
          </el-descriptions-item>
          <el-descriptions-item label="启用状态">
            <el-tag :type="monitorData.enabled ? 'success' : 'danger'">
              {{ monitorData.enabled ? '已启用' : '已禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="警告阈值">
            {{ monitorData.warningThreshold }}{{ monitorData.metricUnit }}
          </el-descriptions-item>
          <el-descriptions-item label="严重阈值">
            {{ monitorData.criticalThreshold }}{{ monitorData.metricUnit }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">
            {{ formatTime(monitorData.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="描述信息" :span="2">
            {{ monitorData.description || '无' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>

    <!-- 历史记录 -->
    <div class="history-section">
      <el-card class="history-card">
        <template #header>
          <div class="card-header">
            <span>监控历史记录</span>
            <el-button type="primary" size="small" @click="handleExport">
              导出数据
            </el-button>
          </div>
        </template>
        
        <el-table
          :data="historyData"
          v-loading="historyLoading"
          style="width: 100%"
          :default-sort="{ prop: 'timestamp', order: 'descending' }"
        >
          <el-table-column prop="timestamp" label="时间" width="180" sortable>
            <template #default="{ row }">
              {{ formatTime(row.timestamp) }}
            </template>
          </el-table-column>
          
          <el-table-column prop="value" label="指标值" width="120">
            <template #default="{ row }">
              <span :class="getValueClassByValue(row.value)">
                {{ row.value }}{{ monitorData.metricUnit }}
              </span>
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusTag(row.status)" size="small">
                {{ getStatusLabel(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="message" label="详细信息" min-width="200">
            <template #default="{ row }">
              {{ row.message || '正常' }}
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="historyPagination.current"
            v-model:page-size="historyPagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="historyPagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleHistorySizeChange"
            @current-change="handleHistoryCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 编辑对话框 -->
    <MonitorDialog 
      v-model="editDialogVisible"
      :data="monitorData"
      mode="edit"
      @success="handleEditSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as echarts from 'echarts'
import MonitorDialog from './components/MonitorDialog.vue'

// 类型定义
interface MonitorData {
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
  monitorInterval: number
  enabled: boolean
  description: string
  createTime: string
}

interface HistoryItem {
  id: number
  timestamp: string
  value: number
  status: number
  message: string
}

// 路由和响应式数据
const router = useRouter()
const loading = ref(false)
const historyLoading = ref(false)
const editDialogVisible = ref(false)
const timeRange = ref('1h')

// 图表实例
const trendChart = ref<HTMLDivElement>()
const statusChart = ref<HTMLDivElement>()
let trendChartInstance: echarts.ECharts | null = null
let statusChartInstance: echarts.ECharts | null = null

// 监控数据
const monitorData = reactive<MonitorData>({
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
  criticalThreshold: 90,
  monitorInterval: 60,
  enabled: true,
  description: '主服务器CPU使用率监控',
  createTime: new Date('2024-01-01').toISOString()
})

// 历史数据
const historyData = ref<HistoryItem[]>([])
const historyPagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 模拟历史数据
const mockHistoryData: HistoryItem[] = Array.from({ length: 100 }, (_, i) => ({
  id: i + 1,
  timestamp: new Date(Date.now() - i * 60000).toISOString(),
  value: Math.random() * 100,
  status: Math.random() > 0.9 ? 1 : Math.random() > 0.95 ? 2 : 0,
  message: Math.random() > 0.9 ? '指标异常' : ''
}))

// 方法定义
const fetchMonitorData = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 更新实时数据
    monitorData.metricValue = Math.random() * 100
    monitorData.status = monitorData.metricValue > monitorData.criticalThreshold! ? 2 : 
                         monitorData.metricValue > monitorData.warningThreshold! ? 1 : 0
    monitorData.monitorTimestamp = new Date().toISOString()
    
  } catch (error) {
    ElMessage.error('获取监控数据失败')
  } finally {
    loading.value = false
  }
}

const fetchHistoryData = async () => {
  historyLoading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 300))
    
    const start = (historyPagination.current - 1) * historyPagination.size
    const end = start + historyPagination.size
    historyData.value = mockHistoryData.slice(start, end)
    historyPagination.total = mockHistoryData.length
    
  } catch (error) {
    ElMessage.error('获取历史数据失败')
  } finally {
    historyLoading.value = false
  }
}

const initCharts = () => {
  if (!trendChart.value || !statusChart.value) return
  
  // 初始化趋势图表
  trendChartInstance = echarts.init(trendChart.value)
  
  // 初始化状态图表
  statusChartInstance = echarts.init(statusChart.value)
  
  updateCharts()
}

const updateCharts = () => {
  if (!trendChartInstance || !statusChartInstance) return
  
  // 更新趋势图表
  const trendOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: Array.from({ length: 24 }, (_, i) => `${i}:00`)
    },
    yAxis: {
      type: 'value',
      name: monitorData.metricUnit
    },
    series: [
      {
        name: '指标值',
        type: 'line',
        smooth: true,
        data: Array.from({ length: 24 }, () => Math.random() * 100),
        itemStyle: {
          color: '#409eff'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
          ])
        }
      },
      {
        name: '警告阈值',
        type: 'line',
        data: Array(24).fill(monitorData.warningThreshold),
        lineStyle: {
          type: 'dashed',
          color: '#e6a23c'
        }
      },
      {
        name: '严重阈值',
        type: 'line',
        data: Array(24).fill(monitorData.criticalThreshold),
        lineStyle: {
          type: 'dashed',
          color: '#f56c6c'
        }
      }
    ]
  }
  
  trendChartInstance.setOption(trendOption)
  
  // 更新状态图表
  const statusData = [
    { value: 85, name: '正常' },
    { value: 10, name: '警告' },
    { value: 5, name: '严重' }
  ]
  
  const statusOption = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '状态分布',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: statusData,
        color: ['#67c23a', '#e6a23c', '#f56c6c']
      }
    ]
  }
  
  statusChartInstance.setOption(statusOption)
}

const handleBack = () => {
  router.back()
}

const handleEdit = () => {
  editDialogVisible.value = true
}

const handleToggleStatus = async () => {
  try {
    await ElMessageBox.confirm(
      `确定${monitorData.enabled ? '禁用' : '启用'}此监控吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    monitorData.enabled = !monitorData.enabled
    ElMessage.success(`${monitorData.enabled ? '启用' : '禁用'}成功`)
    
  } catch (error) {
    // 用户取消操作
  }
}

const handleRefresh = () => {
  fetchMonitorData()
  fetchHistoryData()
  updateCharts()
}

const handleEditSuccess = () => {
  fetchMonitorData()
  updateCharts()
}

const handleTimeRangeChange = () => {
  updateCharts()
}

const handleExport = () => {
  ElMessage.success('导出功能开发中')
}

const handleHistorySizeChange = (size: number) => {
  historyPagination.size = size
  historyPagination.current = 1
  fetchHistoryData()
}

const handleHistoryCurrentChange = (page: number) => {
  historyPagination.current = page
  fetchHistoryData()
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

const getValueClass = (data: MonitorData) => {
  if (data.status === 0) return 'value-normal'
  if (data.status === 1) return 'value-warning'
  if (data.status === 2) return 'value-critical'
  return 'value-unknown'
}

const getValueClassByValue = (value: number) => {
  if (value > monitorData.criticalThreshold!) return 'value-critical'
  if (value > monitorData.warningThreshold!) return 'value-warning'
  return 'value-normal'
}

const getAvailabilityColor = (percentage: number) => {
  if (percentage >= 99) return '#67c23a'
  if (percentage >= 95) return '#e6a23c'
  return '#f56c6c'
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  fetchMonitorData()
  fetchHistoryData()
  
  nextTick(() => {
    initCharts()
  })
  
  // 定时刷新数据
  const timer = setInterval(() => {
    if (monitorData.enabled) {
      fetchMonitorData()
      updateCharts()
    }
  }, monitorData.monitorInterval * 1000)
  
  onUnmounted(() => {
    clearInterval(timer)
    trendChartInstance?.dispose()
    statusChartInstance?.dispose()
  })
})

// 响应窗口大小变化
window.addEventListener('resize', () => {
  trendChartInstance?.resize()
  statusChartInstance?.resize()
})
</script>

<style scoped>
.monitor-detail {
  padding: 0;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-title {
  margin: 0;
  color: #fff;
  font-size: 24px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.info-cards {
  margin-bottom: 20px;
}

.info-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s ease;
}

.info-card:hover {
  background: rgba(255, 255, 255, 0.08);
  transform: translateY(-2px);
}

.card-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  background: rgba(64, 158, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #409eff;
}

.card-content {
  flex: 1;
}

.card-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 4px;
}

.card-value {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  display: flex;
  align-items: center;
  gap: 8px;
}

.availability-text {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  min-width: 50px;
}

.chart-section {
  margin-bottom: 20px;
}

.chart-card {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 8px;
  padding: 20px;
  height: 400px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-header h3 {
  margin: 0;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
}

.chart-container {
  height: calc(100% - 40px);
}

.chart {
  width: 100%;
  height: 100%;
}

.config-section,
.history-section {
  margin-bottom: 20px;
}

.config-card,
.history-card {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.config-card .el-card__header),
:deep(.history-card .el-card__header) {
  background: rgba(255, 255, 255, 0.05);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 15px 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  color: #fff;
  font-weight: 600;
}

:deep(.el-descriptions__label) {
  color: rgba(255, 255, 255, 0.7);
}

:deep(.el-descriptions__content) {
  color: #fff;
}

.pagination-container {
  padding: 20px 0 0;
  display: flex;
  justify-content: center;
}

.value-normal {
  color: #67c23a;
}

.value-warning {
  color: #e6a23c;
}

.value-critical {
  color: #f56c6c;
}

.value-unknown {
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .detail-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .header-left {
    justify-content: space-between;
  }
  
  .header-actions {
    justify-content: center;
  }
  
  .info-card {
    padding: 16px;
  }
  
  .card-icon {
    width: 40px;
    height: 40px;
    font-size: 20px;
  }
  
  .card-value {
    font-size: 16px;
  }
  
  .chart-card {
    height: 300px;
    padding: 16px;
  }
}
</style>