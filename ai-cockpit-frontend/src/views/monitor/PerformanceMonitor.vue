<template>
  <div class="performance-monitor">
    <!-- 筛选工具栏 -->
    <div class="filter-toolbar">
      <el-form :model="filterForm" inline class="filter-form">
        <el-form-item label="性能指标">
          <el-select v-model="filterForm.metricType" placeholder="请选择性能指标" clearable>
            <el-option label="响应时间" value="response_time" />
            <el-option label="吞吐量" value="throughput" />
            <el-option label="错误率" value="error_rate" />
            <el-option label="并发数" value="concurrency" />
            <el-option label="CPU使用率" value="cpu_usage" />
            <el-option label="内存使用率" value="memory_usage" />
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
        
        <el-form-item label="服务类型">
          <el-select v-model="filterForm.serviceType" placeholder="请选择服务类型" clearable>
            <el-option label="API服务" value="api" />
            <el-option label="数据库服务" value="database" />
            <el-option label="缓存服务" value="cache" />
            <el-option label="消息队列" value="mq" />
            <el-option label="存储服务" value="storage" />
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

    <!-- 性能概览卡片 -->
    <div class="overview-cards">
      <el-row :gutter="16">
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon response">
              <i class="el-icon-time"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.responseTime }}ms</div>
              <div class="card-label">平均响应时间</div>
              <div class="card-trend" :class="overview.responseTimeTrend">
                <i :class="overview.responseTimeTrendIcon"></i>
                {{ overview.responseTimeChange }}
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon throughput">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.throughput }}/s</div>
              <div class="card-label">平均吞吐量</div>
              <div class="card-trend" :class="overview.throughputTrend">
                <i :class="overview.throughputTrendIcon"></i>
                {{ overview.throughputChange }}
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon error">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.errorRate }}%</div>
              <div class="card-label">错误率</div>
              <div class="card-trend" :class="overview.errorRateTrend">
                <i :class="overview.errorRateTrendIcon"></i>
                {{ overview.errorRateChange }}
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="12" :sm="6">
          <div class="overview-card">
            <div class="card-icon concurrency">
              <i class="el-icon-user"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.concurrency }}</div>
              <div class="card-label">并发用户数</div>
              <div class="card-trend" :class="overview.concurrencyTrend">
                <i :class="overview.concurrencyTrendIcon"></i>
                {{ overview.concurrencyChange }}
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
              <h3>响应时间趋势</h3>
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
              <div ref="responseTimeChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>吞吐量趋势</h3>
            </div>
            <div class="chart-container">
              <div ref="throughputChart" class="chart"></div>
            </div>
          </div>
        </el-col>
      </el-row>
      
      <el-row :gutter="16" class="mt-16">
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>错误率分布</h3>
            </div>
            <div class="chart-container">
              <div ref="errorRateChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :lg="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>服务性能对比</h3>
            </div>
            <div class="chart-container">
              <div ref="serviceComparisonChart" class="chart"></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 性能排名 -->
    <div class="ranking-section">
      <el-card class="ranking-card">
        <template #header>
          <div class="card-header">
            <span>服务性能排名</span>
          </div>
        </template>
        
        <el-table
          :data="rankingData"
          v-loading="rankingLoading"
          style="width: 100%"
          :default-sort="{ prop: 'responseTime', order: 'ascending' }"
        >
          <el-table-column prop="serviceName" label="服务名称" min-width="150">
            <template #default="{ row }">
              <div class="service-info">
                <div class="service-name">{{ row.serviceName }}</div>
                <div class="service-type">{{ getServiceTypeLabel(row.serviceType) }}</div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="responseTime" label="响应时间(ms)" width="120" sortable>
            <template #default="{ row }">
              <span :class="getResponseTimeClass(row.responseTime)">{{ row.responseTime }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="throughput" label="吞吐量(/s)" width="120" sortable>
            <template #default="{ row }">
              {{ row.throughput }}
            </template>
          </el-table-column>
          
          <el-table-column prop="errorRate" label="错误率" width="100" sortable>
            <template #default="{ row }">
              <span :class="getErrorRateClass(row.errorRate)">{{ row.errorRate }}%</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="availability" label="可用性" width="100" sortable>
            <template #default="{ row }">
              <el-progress 
                :percentage="row.availability" 
                :show-text="false" 
                :stroke-width="8"
                :color="getAvailabilityColor(row.availability)"
              />
              <span class="availability-text">{{ row.availability }}%</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="lastUpdate" label="最后更新" width="180">
            <template #default="{ row }">
              {{ formatTime(row.lastUpdate) }}
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
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

// 类型定义
interface PerformanceData {
  serviceName: string
  serviceType: string
  responseTime: number
  throughput: number
  errorRate: number
  availability: number
  lastUpdate: string
}

interface OverviewData {
  responseTime: number
  responseTimeTrend: string
  responseTimeTrendIcon: string
  responseTimeChange: string
  throughput: number
  throughputTrend: string
  throughputTrendIcon: string
  throughputChange: string
  errorRate: number
  errorRateTrend: string
  errorRateTrendIcon: string
  errorRateChange: string
  concurrency: number
  concurrencyTrend: string
  concurrencyTrendIcon: string
  concurrencyChange: string
}

// 响应式数据
const loading = ref(false)
const rankingLoading = ref(false)
const timeRange = ref('1h')

// 图表实例
const responseTimeChart = ref<HTMLDivElement>()
const throughputChart = ref<HTMLDivElement>()
const errorRateChart = ref<HTMLDivElement>()
const serviceComparisonChart = ref<HTMLDivElement>()

let responseTimeChartInstance: echarts.ECharts | null = null
let throughputChartInstance: echarts.ECharts | null = null
let errorRateChartInstance: echarts.ECharts | null = null
let serviceComparisonChartInstance: echarts.ECharts | null = null

const filterForm = reactive({
  metricType: '',
  timeRange: [],
  serviceType: ''
})

const overview = reactive<OverviewData>({
  responseTime: 0,
  responseTimeTrend: '',
  responseTimeTrendIcon: '',
  responseTimeChange: '',
  throughput: 0,
  throughputTrend: '',
  throughputTrendIcon: '',
  throughputChange: '',
  errorRate: 0,
  errorRateTrend: '',
  errorRateTrendIcon: '',
  errorRateChange: '',
  concurrency: 0,
  concurrencyTrend: '',
  concurrencyTrendIcon: '',
  concurrencyChange: ''
})

const rankingData = ref<PerformanceData[]>([])

// 模拟数据
const mockRankingData: PerformanceData[] = [
  {
    serviceName: '用户认证服务',
    serviceType: 'api',
    responseTime: 45,
    throughput: 1200,
    errorRate: 0.1,
    availability: 99.9,
    lastUpdate: new Date().toISOString()
  },
  {
    serviceName: 'AI模型服务',
    serviceType: 'api',
    responseTime: 120,
    throughput: 800,
    errorRate: 0.5,
    availability: 99.8,
    lastUpdate: new Date().toISOString()
  },
  {
    serviceName: '文件存储服务',
    serviceType: 'storage',
    responseTime: 80,
    throughput: 1500,
    errorRate: 0.2,
    availability: 99.7,
    lastUpdate: new Date().toISOString()
  },
  {
    serviceName: 'MySQL数据库',
    serviceType: 'database',
    responseTime: 25,
    throughput: 2000,
    errorRate: 0.05,
    availability: 99.95,
    lastUpdate: new Date().toISOString()
  },
  {
    serviceName: 'Redis缓存',
    serviceType: 'cache',
    responseTime: 5,
    throughput: 5000,
    errorRate: 0.01,
    availability: 99.99,
    lastUpdate: new Date().toISOString()
  }
]

// 方法定义
const fetchData = async () => {
  loading.value = true
  rankingLoading.value = true
  
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 更新概览数据
    updateOverviewData()
    
    // 更新排名数据
    rankingData.value = mockRankingData
    
  } catch (error) {
    ElMessage.error('获取性能数据失败')
  } finally {
    loading.value = false
    rankingLoading.value = false
  }
}

const updateOverviewData = () => {
  // 模拟实时数据更新
  overview.responseTime = Math.random() * 200 + 50
  overview.responseTimeTrend = Math.random() > 0.5 ? 'up' : 'down'
  overview.responseTimeTrendIcon = Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom'
  overview.responseTimeChange = Math.random() > 0.5 ? '+12ms' : '-8ms'
  
  overview.throughput = Math.random() * 2000 + 1000
  overview.throughputTrend = Math.random() > 0.5 ? 'up' : 'down'
  overview.throughputTrendIcon = Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom'
  overview.throughputChange = Math.random() > 0.5 ? '+150/s' : '-80/s'
  
  overview.errorRate = Math.random() * 2
  overview.errorRateTrend = Math.random() > 0.5 ? 'up' : 'down'
  overview.errorRateTrendIcon = Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom'
  overview.errorRateChange = Math.random() > 0.5 ? '+0.2%' : '-0.1%'
  
  overview.concurrency = Math.random() * 500 + 100
  overview.concurrencyTrend = Math.random() > 0.5 ? 'up' : 'down'
  overview.concurrencyTrendIcon = Math.random() > 0.5 ? 'el-icon-top' : 'el-icon-bottom'
  overview.concurrencyChange = Math.random() > 0.5 ? '+25' : '-15'
}

const initCharts = () => {
  if (!responseTimeChart.value || !throughputChart.value || 
      !errorRateChart.value || !serviceComparisonChart.value) return
  
  // 初始化图表实例
  responseTimeChartInstance = echarts.init(responseTimeChart.value)
  throughputChartInstance = echarts.init(throughputChart.value)
  errorRateChartInstance = echarts.init(errorRateChart.value)
  serviceComparisonChartInstance = echarts.init(serviceComparisonChart.value)
  
  updateCharts()
}

const updateCharts = () => {
  if (!responseTimeChartInstance || !throughputChartInstance || 
      !errorRateChartInstance || !serviceComparisonChartInstance) return
  
  // 响应时间图表
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
      data: Array.from({ length: 24 }, (_, i) => `${i}:00`)
    },
    yAxis: {
      type: 'value',
      name: 'ms'
    },
    series: [{
      name: '响应时间',
      type: 'line',
      smooth: true,
      data: Array.from({ length: 24 }, () => Math.random() * 200 + 50),
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
  
  responseTimeChartInstance.setOption(responseTimeOption)
  
  // 吞吐量图表
  const throughputOption = {
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
      name: '请求/秒'
    },
    series: [{
      name: '吞吐量',
      type: 'line',
      smooth: true,
      data: Array.from({ length: 24 }, () => Math.random() * 2000 + 1000),
      itemStyle: {
        color: '#67c23a'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
          { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }
        ])
      }
    }]
  }
  
  throughputChartInstance.setOption(throughputOption)
  
  // 错误率图表
  const errorRateOption = {
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
      name: '%'
    },
    series: [{
      name: '错误率',
      type: 'line',
      smooth: true,
      data: Array.from({ length: 24 }, () => Math.random() * 2),
      itemStyle: {
        color: '#f56c6c'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(245, 108, 108, 0.3)' },
          { offset: 1, color: 'rgba(245, 108, 108, 0.1)' }
        ])
      }
    }]
  }
  
  errorRateChartInstance.setOption(errorRateOption)
  
  // 服务对比图表
  const comparisonOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['响应时间(ms)', '吞吐量(/s)', '错误率(%)']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: mockRankingData.map(item => item.serviceName)
    },
    yAxis: [
      {
        type: 'value',
        name: '响应时间/错误率',
        axisLabel: {
          formatter: '{value}'
        }
      },
      {
        type: 'value',
        name: '吞吐量',
        axisLabel: {
          formatter: '{value}'
        }
      }
    ],
    series: [
      {
        name: '响应时间(ms)',
        type: 'bar',
        data: mockRankingData.map(item => item.responseTime),
        itemStyle: {
          color: '#409eff'
        }
      },
      {
        name: '吞吐量(/s)',
        type: 'bar',
        yAxisIndex: 1,
        data: mockRankingData.map(item => item.throughput),
        itemStyle: {
          color: '#67c23a'
        }
      },
      {
        name: '错误率(%)',
        type: 'line',
        data: mockRankingData.map(item => item.errorRate),
        itemStyle: {
          color: '#f56c6c'
        }
      }
    ]
  }
  
  serviceComparisonChartInstance.setOption(comparisonOption)
}

const handleSearch = () => {
  fetchData()
}

const handleReset = () => {
  Object.assign(filterForm, {
    metricType: '',
    timeRange: [],
    serviceType: ''
  })
  fetchData()
}

const handleRefresh = () => {
  fetchData()
  updateCharts()
}

const handleExport = () => {
  ElMessage.success('导出功能开发中')
}

const handleTimeRangeChange = () => {
  updateCharts()
}

const handleViewDetail = (row: PerformanceData) => {
  // 跳转到服务详情页面
  console.log('查看服务详情:', row)
}

// 工具函数
const getServiceTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    'api': 'API服务',
    'database': '数据库服务',
    'cache': '缓存服务',
    'mq': '消息队列',
    'storage': '存储服务'
  }
  return map[type] || type
}

const getResponseTimeClass = (time: number) => {
  if (time < 50) return 'value-excellent'
  if (time < 100) return 'value-good'
  if (time < 200) return 'value-warning'
  return 'value-critical'
}

const getErrorRateClass = (rate: number) => {
  if (rate < 0.1) return 'value-excellent'
  if (rate < 0.5) return 'value-good'
  if (rate < 1) return 'value-warning'
  return 'value-critical'
}

const getAvailabilityColor = (percentage: number) => {
  if (percentage >= 99.9) return '#67c23a'
  if (percentage >= 99.5) return '#e6a23c'
  return '#f56c6c'
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
    responseTimeChartInstance?.dispose()
    throughputChartInstance?.dispose()
    errorRateChartInstance?.dispose()
    serviceComparisonChartInstance?.dispose()
  })
})

// 响应窗口大小变化
window.addEventListener('resize', () => {
  responseTimeChartInstance?.resize()
  throughputChartInstance?.resize()
  errorRateChartInstance?.resize()
  serviceComparisonChartInstance?.resize()
})
</script>

<style scoped>
.performance-monitor {
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

.card-icon.response {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.card-icon.throughput {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.card-icon.error {
  background: linear-gradient(135deg, #f56c6c, #f78989);
}

.card-icon.concurrency {
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

.ranking-section {
  margin-bottom: 20px;
}

.ranking-card {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.ranking-card .el-card__header) {
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

.service-info {
  line-height: 1.4;
}

.service-name {
  color: #fff;
  font-weight: 500;
}

.service-type {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

.availability-text {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
  min-width: 40px;
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
</style>