<template>
  <div class="dashboard-container">
    <!-- 顶部统计卡片 -->
    <div class="stats-grid">
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon model-icon">
            <el-icon><Cpu /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalModels }}</div>
            <div class="stat-label">模型总数</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon dataset-icon">
            <el-icon><DataBoard /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalDatasets }}</div>
            <div class="stat-label">数据集总数</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon training-icon">
            <el-icon><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.runningTasks }}</div>
            <div class="stat-label">运行中任务</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon deployment-icon">
            <el-icon><Ship /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.activeDeployments }}</div>
            <div class="stat-label">活跃部署</div>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-grid">
      <!-- 模型类型分布 -->
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="chart-header">
            <span class="chart-title">模型类型分布</span>
          </div>
        </template>
        <div class="chart-container">
          <v-chart :option="modelTypeChart" autoresize />
        </div>
      </el-card>
      
      <!-- 任务状态分布 -->
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="chart-header">
            <span class="chart-title">任务状态分布</span>
          </div>
        </template>
        <div class="chart-container">
          <v-chart :option="taskStatusChart" autoresize />
        </div>
      </el-card>
      
      <!-- 资源使用情况 -->
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="chart-header">
            <span class="chart-title">资源使用情况</span>
          </div>
        </template>
        <div class="chart-container">
          <v-chart :option="resourceUsageChart" autoresize />
        </div>
      </el-card>
      
      <!-- 最近任务 -->
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="chart-header">
            <span class="chart-title">最近任务</span>
            <router-link to="/model/training" class="view-all">查看全部</router-link>
          </div>
        </template>
        <div class="recent-tasks">
          <div 
            v-for="task in recentTasks" 
            :key="task.id" 
            class="task-item"
          >
            <div class="task-info">
              <div class="task-name">{{ task.name }}</div>
              <div class="task-status" :class="`status-${task.status}`">
                {{ getStatusText(task.status) }}
              </div>
            </div>
            <div class="task-progress">
              <el-progress 
                :percentage="task.progress" 
                :status="getProgressStatus(task.status)"
                :show-text="false"
              />
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart, LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import VChart from 'vue-echarts'

use([
  CanvasRenderer,
  PieChart,
  BarChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

// 统计数据
const stats = reactive({
  totalModels: 0,
  totalDatasets: 0,
  runningTasks: 0,
  activeDeployments: 0
})

// 最近任务
const recentTasks = ref([])

// 模型类型分布图表配置
const modelTypeChart = reactive({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    right: 10,
    top: 'center'
  },
  series: [
    {
      name: '模型类型',
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
          fontSize: 18,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: 35, name: '分类模型' },
        { value: 25, name: '回归模型' },
        { value: 20, name: '聚类模型' },
        { value: 15, name: '深度学习' },
        { value: 5, name: '其他' }
      ]
    }
  ]
})

// 任务状态分布图表配置
const taskStatusChart = reactive({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
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
    data: ['准备中', '运行中', '已完成', '失败', '取消']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '任务数量',
      type: 'bar',
      barWidth: '60%',
      data: [12, 8, 45, 3, 2]
    }
  ]
})

// 资源使用情况图表配置
const resourceUsageChart = reactive({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['CPU使用率', '内存使用率', 'GPU使用率']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: 'CPU使用率',
      type: 'line',
      smooth: true,
      data: [30, 25, 40, 35, 45, 30]
    },
    {
      name: '内存使用率',
      type: 'line',
      smooth: true,
      data: [45, 40, 50, 55, 60, 50]
    },
    {
      name: 'GPU使用率',
      type: 'line',
      smooth: true,
      data: [20, 15, 25, 30, 35, 25]
    }
  ]
})

// 获取状态文本
const getStatusText = (status: number) => {
  const statusMap = {
    1: '准备中',
    2: '运行中',
    3: '已完成',
    4: '失败',
    5: '取消'
  }
  return statusMap[status] || '未知'
}

// 获取进度条状态
const getProgressStatus = (status: number) => {
  if (status === 3) return 'success'
  if (status === 4) return 'exception'
  return ''
}

// 加载数据
const loadData = async () => {
  try {
    // 模拟加载数据
    stats.totalModels = 125
    stats.totalDatasets = 89
    stats.runningTasks = 8
    stats.activeDeployments = 15
    
    recentTasks.value = [
      { id: 1, name: '图像分类模型训练', status: 2, progress: 65 },
      { id: 2, name: '文本情感分析', status: 3, progress: 100 },
      { id: 3, name: '目标检测模型', status: 1, progress: 0 },
      { id: 4, name: '语音识别模型', status: 2, progress: 45 }
    ]
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  :deep(.el-card__body) {
    padding: 20px;
  }
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  
  &.model-icon {
    background: linear-gradient(135deg, #409EFF, #67C23A);
  }
  
  &.dataset-icon {
    background: linear-gradient(135deg, #E6A23C, #F56C6C);
  }
  
  &.training-icon {
    background: linear-gradient(135deg, #909399, #36A2EB);
  }
  
  &.deployment-icon {
    background: linear-gradient(135deg, #9966FF, #FF6384);
  }
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.chart-card {
  :deep(.el-card__header) {
    padding: 16px 20px;
    border-bottom: 1px solid #ebeef5;
  }
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.view-all {
  font-size: 14px;
  color: #409EFF;
  text-decoration: none;
  
  &:hover {
    text-decoration: underline;
  }
}

.chart-container {
  height: 300px;
}

.recent-tasks {
  .task-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
  }
  
  .task-info {
    flex: 1;
  }
  
  .task-name {
    font-size: 14px;
    color: #303133;
    margin-bottom: 4px;
  }
  
  .task-status {
    font-size: 12px;
    
    &.status-1 { color: #E6A23C; }
    &.status-2 { color: #409EFF; }
    &.status-3 { color: #67C23A; }
    &.status-4 { color: #F56C6C; }
    &.status-5 { color: #909399; }
  }
  
  .task-progress {
    width: 100px;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .charts-grid {
    grid-template-columns: 1fr;
  }
}
</style>