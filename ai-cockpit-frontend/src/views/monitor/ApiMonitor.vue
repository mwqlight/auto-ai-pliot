<template>
  <div class="api-monitor">
    <!-- 筛选工具栏 -->
    <div class="filter-toolbar">
      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="API路径">
          <el-input
            v-model="filterForm.apiPath"
            placeholder="请输入API路径"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        
        <el-form-item label="HTTP方法">
          <el-select v-model="filterForm.httpMethod" placeholder="请选择HTTP方法" clearable>
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
            <el-option label="PATCH" value="PATCH" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="状态码">
          <el-select v-model="filterForm.statusCode" placeholder="请选择状态码" clearable>
            <el-option label="2xx 成功" value="2xx" />
            <el-option label="4xx 客户端错误" value="4xx" />
            <el-option label="5xx 服务端错误" value="5xx" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="filterForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
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
        <el-button type="success" @click="handleExport">
          <i class="el-icon-download"></i>
          导出数据
        </el-button>
        <el-button type="warning" @click="handleRefresh">
          <i class="el-icon-refresh"></i>
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- API概览卡片 -->
    <div class="overview-cards">
      <el-row :gutter="16">
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon total">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.totalRequests }}</div>
              <div class="card-label">总请求数</div>
              <div class="card-trend" :class="overview.totalRequestsTrend">
                <i :class="overview.totalRequestsTrendIcon"></i>
                {{ overview.totalRequestsChange }}
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon success">
              <i class="el-icon-success"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.successRate }}%</div>
              <div class="card-label">成功率</div>
              <div class="card-trend" :class="overview.successRateTrend">
                <i :class="overview.successRateTrendIcon"></i>
                {{ overview.successRateChange }}
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon error">
              <i class="el-icon-error"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.errorCount }}</div>
              <div class="card-label">错误数</div>
              <div class="card-trend" :class="overview.errorCountTrend">
                <i :class="overview.errorCountTrendIcon"></i>
                {{ overview.errorCountChange }}
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon response">
              <i class="el-icon-time"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.avgResponseTime }}ms</div>
              <div class="card-label">平均响应时间</div>
              <div class="card-trend" :class="overview.avgResponseTimeTrend">
                <i :class="overview.avgResponseTimeTrendIcon"></i>
                {{ overview.avgResponseTimeChange }}
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="chart-section">
      <el-row :gutter="16">
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>请求量趋势</h3>
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
              <div ref="requestTrendChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>状态码分布</h3>
            </div>
            <div class="chart-container">
              <div ref="statusCodeChart" class="chart"></div>
            </div>
          </div>
        </el-col>
      </el-row>
      
      <el-row :gutter="16" class="mt-16">
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>响应时间分布</h3>
            </div>
            <div class="chart-container">
              <div ref="responseTimeChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>HTTP方法分布</h3>
            </div>
            <div class="chart-container">
              <div ref="methodChart" class="chart"></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- API列表 -->
    <div class="api-list-section">
      <el-card class="api-list-card">
        <template #header>
          <div class="card-header">
            <span>API调用列表</span>
            <div class="header-actions">
              <el-button size="small" @click="handleClearLogs">
                <i class="el-icon-delete"></i>
                清空日志
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table
          :data="apiList"
          v-loading="listLoading"
          style="width: 100%"
          :default-sort="{ prop: 'timestamp', order: 'descending' }"
        >
          <el-table-column prop="timestamp" label="时间" width="180" sortable>
            <template #default="{ row }">
              {{ formatTime(row.timestamp) }}
            </template>
          </el-table-column>
          
          <el-table-column prop="method" label="方法" width="80">
            <template #default="{ row }">
              <el-tag 
                :type="getMethodType(row.method)"
                size="small"
              >
                {{ row.method }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="path" label="API路径" min-width="200">
            <template #default="{ row }">
              <span class="api-path">{{ row.path }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="statusCode" label="状态码" width="100">
            <template #default="{ row }">
              <el-tag 
                :type="getStatusCodeType(row.statusCode)"
                size="small"
              >
                {{ row.statusCode }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="responseTime" label="响应时间" width="120" sortable>
            <template #default="{ row }">
              <span :class="getResponseTimeClass(row.responseTime)">{{ row.responseTime }}ms</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="clientIp" label="客户端IP" width="140">
            <template #default="{ row }">
              {{ row.clientIp }}
            </template>
          </el-table-column>
          
          <el-table-column prop="userAgent" label="用户代理" min-width="200" show-overflow-tooltip>
            <template #default="{ row }">
              <span class="user-agent">{{ row.userAgent }}</span>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleViewDetail(row)">
                详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- API详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="API调用详情"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="currentDetail" class="api-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="请求时间">
            {{ formatTime(currentDetail.timestamp) }}
          </el-descriptions-item>
          <el-descriptions-item label="HTTP方法">
            <el-tag :type="getMethodType(currentDetail.method)">
              {{ currentDetail.method }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="API路径">
            {{ currentDetail.path }}
          </el-descriptions-item>
          <el-descriptions-item label="状态码">
            <el-tag :type="getStatusCodeType(currentDetail.statusCode)">
              {{ currentDetail.statusCode }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="响应时间">
            <span :class="getResponseTimeClass(currentDetail.responseTime)">
              {{ currentDetail.responseTime }}ms
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="客户端IP">
            {{ currentDetail.clientIp }}
          </el-descriptions-item>
          <el-descriptions-item label="用户代理" :span="2">
            {{ currentDetail.userAgent }}
          </el-descriptions-item>
          <el-descriptions-item label="请求头" :span="2">
            <pre class="json-pre">{{ JSON.stringify(currentDetail.requestHeaders, null, 2) }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="响应头" :span="2">
            <pre class="json-pre">{{ JSON.stringify(currentDetail.responseHeaders, null, 2) }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="请求体" :span="2" v-if="currentDetail.requestBody">
            <pre class="json-pre">{{ JSON.stringify(currentDetail.requestBody, null, 2) }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="响应体" :span="2" v-if="currentDetail.responseBody">
            <pre class="json-pre">{{ JSON.stringify(currentDetail.responseBody, null, 2) }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as echarts from 'echarts'

// 类型定义
interface ApiLog {
  id: string
  timestamp: string
  method: string
  path: string
  statusCode: number
  responseTime: number
  clientIp: string
  userAgent: string
  requestHeaders: Record<string, string>
  responseHeaders: Record<string, string>
  requestBody?: any
  responseBody?: any
}

interface OverviewData {
  totalRequests: number
  totalRequestsTrend: string
  totalRequestsTrendIcon: string
  totalRequestsChange: string
  successRate: number
  successRateTrend: string
  successRateTrendIcon: string
  successRateChange: string
  errorCount: number
  errorCountTrend: string
  errorCountTrendIcon: string
  errorCountChange: string
  avgResponseTime: number
  avgResponseTimeTrend: string
  avgResponseTimeTrendIcon: string
  avgResponseTimeChange: string
}

// 响应式数据
const loading = ref(false)
const listLoading = ref(false)
const detailDialogVisible = ref(false)
const timeRange = ref('1h')
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

// 图表实例
const requestTrendChart = ref<HTMLDivElement>()
const statusCodeChart = ref<HTMLDivElement>()
const responseTimeChart = ref<HTMLDivElement>()
const methodChart = ref<HTMLDivElement>()

let requestTrendChartInstance: echarts.ECharts | null = null
let statusCodeChartInstance: echarts.ECharts | null = null
let responseTimeChartInstance: echarts.ECharts | null = null
let methodChartInstance: echarts.ECharts | null = null

const filterForm = reactive({
  apiPath: '',
  httpMethod: '',
  statusCode: '',
  timeRange: []
})

const overview = reactive<OverviewData>({
  totalRequests: 0,
  totalRequestsTrend: '',
  totalRequestsTrendIcon: '',
  totalRequestsChange: '',
  successRate: 0,
  successRateTrend: '',
  successRateTrendIcon: '',
  successRateChange: '',
  errorCount: 0,
  errorCountTrend: '',
  errorCountTrendIcon: '',
  errorCountChange: '',
  avgResponseTime: 0,
  avgResponseTimeTrend: '',
  avgResponseTimeTrendIcon: '',
  avgResponseTimeChange: ''
})

const apiList = ref<ApiLog[]>([])
const currentDetail = ref<ApiLog | null>(null)

// 模拟数据
const mockApiLogs: ApiLog[] = Array.from({ length: 100 }, (_, i) => ({
  id: `log-${i}`,
  timestamp: new Date(Date.now() - Math.random() * 24 * 60 * 60 * 1000).toISOString(),
  method: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH'][Math.floor(Math.random() * 5)],
  path: ['/api/v1/users', '/api/v1/models', '/api/v1/datasets', '/api/v1/applications', '/api/v1/auth/login'][Math.floor(Math.random() * 5)],
  statusCode: Math.random() > 0.1 ? 200 : Math.random() > 0.5 ? 400 : 500,
  responseTime: Math.random() * 500 + 50,
  clientIp: `192.168.1.${Math.floor(Math.random() * 255)}`,
  userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
  requestHeaders: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer token123',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
  },
  responseHeaders: {
    'Content-Type': 'application/json',
    'X-Request-ID': `req-${i}`
  },
  requestBody: Math.random() > 0.5 ? { username: 'test', password: '******' } : undefined,
  responseBody: { code: 200, message: 'success', data: {} }
}))

// 方法定义
const fetchData = async () => {
  loading.value = true
  listLoading.value = true
  
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 更新概览数据
    updateOverviewData()
    
    // 更新列表数据
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    apiList.value = mockApiLogs.slice(start, end)
    total.value = mockApiLogs.length
    
  } catch (error) {
    ElMessage.error('获取API监控数据失败')
  } finally {
    loading.value = false
    listLoading.value = false
  }
}

const updateOverviewData = () => {
  // 模拟实时数据更新
  overview.totalRequests = Math.floor(Math.random() * 10000) + 5000
  overview.totalRequestsTrend = Math.random() > 0.5 ? 'up' : 'down'
  overview.totalRequestsTrendIcon = Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom'
  overview.totalRequestsChange = Math.random() > 0.5 ? '+125' : '-80'
  
  overview.successRate = Math.random() * 20 + 90
  overview.successRateTrend = Math.random() > 0.5 ? 'up' : 'down'
  overview.successRateTrendIcon = Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom'
  overview.successRateChange = Math.random() > 0.5 ? '+1.2%' : '-0.8%'
  
  overview.errorCount = Math.floor(Math.random() * 100)
  overview.errorCountTrend = Math.random() > 0.5 ? 'up' : 'down'
  overview.errorCountTrendIcon = Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom'
  overview.errorCountChange = Math.random() > 0.5 ? '+5' : '-3'
  
  overview.avgResponseTime = Math.random() * 200 + 50
  overview.avgResponseTimeTrend = Math.random() > 0.5 ? 'up' : 'down'
  overview.avgResponseTimeTrendIcon = Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom'
  overview.avgResponseTimeChange = Math.random() > 0.5 ? '+12ms' : '-8ms'
}

const initCharts = () => {
  if (!requestTrendChart.value || !statusCodeChart.value || 
      !responseTimeChart.value || !methodChart.value) return
  
  // 初始化图表实例
  requestTrendChartInstance = echarts.init(requestTrendChart.value)
  statusCodeChartInstance = echarts.init(statusCodeChart.value)
  responseTimeChartInstance = echarts.init(responseTimeChart.value)
  methodChartInstance = echarts.init(methodChart.value)
  
  updateCharts()
}

const updateCharts = () => {
  if (!requestTrendChartInstance || !statusCodeChartInstance || 
      !responseTimeChartInstance || !methodChartInstance) return
  
  // 请求趋势图表
  const requestTrendOption = {
    tooltip: {
      trigger: 'axis'
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
      name: '请求数'
    },
    series: [{
      name: '请求量',
      type: 'line',
      smooth: true,
      data: Array.from({ length: 24 }, () => Math.random() * 1000 + 500),
      itemStyle: {
        color: '#409eff'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
          { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
        ])
      }
    }]
  }
  
  requestTrendChartInstance.setOption(requestTrendOption)
  
  // 状态码分布图表
  const statusCodeOption = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      name: '状态码分布',
      type: 'pie',
      radius: '50%',
      data: [
        { value: 85, name: '2xx 成功' },
        { value: 10, name: '4xx 客户端错误' },
        { value: 5, name: '5xx 服务端错误' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  }
  
  statusCodeChartInstance.setOption(statusCodeOption)
  
  // 响应时间分布图表
  const responseTimeOption = {
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['<100ms', '100-500ms', '500ms-1s', '1-5s', '>5s']
    },
    yAxis: {
      type: 'value',
      name: '请求数'
    },
    series: [{
      name: '响应时间分布',
      type: 'bar',
      data: [1200, 800, 300, 100, 50],
      itemStyle: {
        color: '#67c23a'
      }
    }]
  }
  
  responseTimeChartInstance.setOption(responseTimeOption)
  
  // HTTP方法分布图表
  const methodOption = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      name: 'HTTP方法',
      type: 'pie',
      radius: '50%',
      data: [
        { value: 60, name: 'GET' },
        { value: 25, name: 'POST' },
        { value: 8, name: 'PUT' },
        { value: 5, name: 'DELETE' },
        { value: 2, name: 'PATCH' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  }
  
  methodChartInstance.setOption(methodOption)
}

const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(filterForm, {
    apiPath: '',
    httpMethod: '',
    statusCode: '',
    timeRange: []
  })
  currentPage.value = 1
  fetchData()
}

const handleRefresh = () => {
  fetchData()
  updateCharts()
}

const handleExport = () => {
  ElMessage.success('导出功能开发中')
}

const handleClearLogs = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有API日志吗？此操作不可恢复。', '确认清空', {
      type: 'warning'
    })
    
    ElMessage.success('日志清空成功')
    fetchData()
  } catch {
    // 用户取消操作
  }
}

const handleTimeRangeChange = () => {
  updateCharts()
}

const handleSizeChange = (newSize: number) => {
  pageSize.value = newSize
  currentPage.value = 1
  fetchData()
}

const handleCurrentChange = (newPage: number) => {
  currentPage.value = newPage
  fetchData()
}

const handleViewDetail = (row: ApiLog) => {
  currentDetail.value = row
  detailDialogVisible.value = true
}

// 工具函数
const getMethodType = (method: string) => {
  const map: Record<string, string> = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger',
    'PATCH': 'info'
  }
  return map[method] || 'info'
}

const getStatusCodeType = (statusCode: number) => {
  if (statusCode >= 200 && statusCode < 300) return 'success'
  if (statusCode >= 400 && statusCode < 500) return 'warning'
  if (statusCode >= 500) return 'danger'
  return 'info'
}

const getResponseTimeClass = (time: number) => {
  if (time < 100) return 'value-excellent'
  if (time < 500) return 'value-good'
  if (time < 1000) return 'value-warning'
  return 'value-critical'
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  fetchData()
  
  nextTick(() => {
    initCharts()
  })
  
  // 定时刷新数据
  const timer = setInterval(() => {
    fetchData()
    updateCharts()
  }, 30000) // 30秒刷新一次
  
  onUnmounted(() => {
    clearInterval(timer)
    requestTrendChartInstance?.dispose()
    statusCodeChartInstance?.dispose()
    responseTimeChartInstance?.dispose()
    methodChartInstance?.dispose()
  })
})

// 响应窗口大小变化
window.addEventListener('resize', () => {
  requestTrendChartInstance?.resize()
  statusCodeChartInstance?.resize()
  responseTimeChartInstance?.resize()
  methodChartInstance?.resize()
})
</script>

<style scoped>
.api-monitor {
  padding: 0;
}

.filter-toolbar {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.chart-card {
  background: rgba(255, 255, 255, 0.02);
  border-radius: 8px;
  padding: 20px;
  height: 400px;
  margin-bottom: 16px;
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

.mt-16 {
  margin-top: 16px;
}

.api-list-section {
  margin-bottom: 20px;
}

.api-list-card {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.api-list-card .el-card__header) {
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

.api-path {
  font-family: 'Courier New', monospace;
  color: #67c23a;
}

.user-agent {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.api-detail {
  max-height: 500px;
  overflow-y: auto;
}

.json-pre {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  padding: 12px;
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
  overflow-x: auto;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.value-excellent {
  color: #67c23a;
  font-weight: 600;
}

.value-good {
  color: #409eff;
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
  
  .chart-card {
    height: 300px;
    padding: 16px;
  }
}

@media (max-width: 480px) {
  .filter-form {
    flex-direction: column;
    align-items: stretch;
  }
  
  .filter-form :deep(.el-form-item) {
    margin-right: 0;
    margin-bottom: 12px;
  }
  
  .overview-card {
    flex-direction: column;
    text-align: center;
    gap: 12px;
  }
  
  .card-content {
    text-align: center;
  }
}
</style>20px;
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

.card-icon.total {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.card-icon.success {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.card-icon.error {
  background: linear-gradient(135deg, #f56c6c, #f78989);
}

.card-icon.response {
  background: linear-gradient(135deg, #e6a23c, #ebb563);
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

.chart-section {
  margin-bottom: 