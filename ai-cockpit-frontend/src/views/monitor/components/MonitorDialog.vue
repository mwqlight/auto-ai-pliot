<template>
  <el-dialog
    v-model="visible"
    :title="dialogTitle"
    width="600px"
    :before-close="handleClose"
    destroy-on-close
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
      label-position="left"
    >
      <el-form-item label="监控类型" prop="monitorType">
        <el-select 
          v-model="formData.monitorType" 
          placeholder="请选择监控类型"
          style="width: 100%"
          @change="handleMonitorTypeChange"
        >
          <el-option 
            v-for="item in monitorTypeOptions" 
            :key="item.value" 
            :label="item.label" 
            :value="item.value" 
          />
        </el-select>
      </el-form-item>

      <el-form-item label="目标类型" prop="targetType">
        <el-select 
          v-model="formData.targetType" 
          placeholder="请选择目标类型"
          style="width: 100%"
          @change="handleTargetTypeChange"
        >
          <el-option 
            v-for="item in targetTypeOptions" 
            :key="item.value" 
            :label="item.label" 
            :value="item.value" 
          />
        </el-select>
      </el-form-item>

      <el-form-item label="监控目标" prop="targetId">
        <el-select 
          v-model="formData.targetId" 
          placeholder="请选择监控目标"
          style="width: 100%"
          filterable
        >
          <el-option 
            v-for="item in targetOptions" 
            :key="item.id" 
            :label="item.name" 
            :value="item.id" 
          />
        </el-select>
      </el-form-item>

      <el-form-item label="监控名称" prop="targetName">
        <el-input 
          v-model="formData.targetName" 
          placeholder="请输入监控名称"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="指标单位" prop="metricUnit">
        <el-input 
          v-model="formData.metricUnit" 
          placeholder="请输入指标单位"
          maxlength="10"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="警告阈值" prop="warningThreshold">
            <el-input 
              v-model.number="formData.warningThreshold" 
              placeholder="请输入警告阈值"
              type="number"
              :min="0"
              :max="100"
            >
              <template #append>{{ getThresholdUnit() }}</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="严重阈值" prop="criticalThreshold">
            <el-input 
              v-model.number="formData.criticalThreshold" 
              placeholder="请输入严重阈值"
              type="number"
              :min="0"
              :max="100"
            >
              <template #append>{{ getThresholdUnit() }}</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="监控间隔" prop="monitorInterval">
        <el-input 
          v-model.number="formData.monitorInterval" 
          placeholder="请输入监控间隔（秒）"
          type="number"
          :min="30"
          :max="3600"
        >
          <template #append>秒</template>
        </el-input>
      </el-form-item>

      <el-form-item label="启用状态" prop="enabled">
        <el-switch 
          v-model="formData.enabled" 
          active-text="启用" 
          inactive-text="禁用"
        />
      </el-form-item>

      <el-form-item label="描述信息" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入监控描述信息"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          {{ mode === 'add' ? '添加' : '保存' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, nextTick } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'

interface Props {
  modelValue: boolean
  data?: any
  mode: 'add' | 'edit'
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}

interface FormData {
  monitorType: string
  targetType: string
  targetId: number | null
  targetName: string
  metricUnit: string
  warningThreshold: number | null
  criticalThreshold: number | null
  monitorInterval: number
  enabled: boolean
  description: string
}

interface TargetOption {
  id: number
  name: string
  type: string
}

// Props 和 Emits
const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 响应式数据
const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const formRef = ref<FormInstance>()
const loading = ref(false)

// 表单数据
const formData = reactive<FormData>({
  monitorType: '',
  targetType: '',
  targetId: null,
  targetName: '',
  metricUnit: '',
  warningThreshold: null,
  criticalThreshold: null,
  monitorInterval: 60,
  enabled: true,
  description: ''
})

// 选项数据
const monitorTypeOptions = [
  { label: 'CPU使用率', value: 'cpu' },
  { label: '内存使用率', value: 'memory' },
  { label: '磁盘使用率', value: 'disk' },
  { label: '网络流量', value: 'network' },
  { label: 'GPU使用率', value: 'gpu' },
  { label: '服务状态', value: 'service' },
  { label: '数据库连接数', value: 'database' },
  { label: '缓存命中率', value: 'cache' }
]

const targetTypeOptions = [
  { label: '服务器', value: 'server' },
  { label: '容器', value: 'container' },
  { label: '服务', value: 'service' },
  { label: '数据库', value: 'database' },
  { label: '缓存', value: 'cache' },
  { label: '负载均衡器', value: 'loadbalancer' },
  { label: '消息队列', value: 'mq' },
  { label: '存储服务', value: 'storage' }
]

const targetOptions = ref<TargetOption[]>([])

// 模拟目标数据
const mockTargets: Record<string, TargetOption[]> = {
  server: [
    { id: 1, name: '主服务器-01', type: 'server' },
    { id: 2, name: '备份服务器-02', type: 'server' },
    { id: 3, name: '计算节点-01', type: 'server' },
    { id: 4, name: '存储节点-01', type: 'server' }
  ],
  container: [
    { id: 101, name: '应用容器-nginx', type: 'container' },
    { id: 102, name: '应用容器-api', type: 'container' },
    { id: 103, name: '应用容器-db', type: 'container' },
    { id: 104, name: '应用容器-cache', type: 'container' }
  ],
  service: [
    { id: 201, name: 'AI模型服务', type: 'service' },
    { id: 202, name: '用户认证服务', type: 'service' },
    { id: 203, name: '文件存储服务', type: 'service' },
    { id: 204, name: '消息推送服务', type: 'service' }
  ],
  database: [
    { id: 301, name: 'MySQL主库', type: 'database' },
    { id: 302, name: 'MySQL从库', type: 'database' },
    { id: 303, name: 'Redis缓存', type: 'database' },
    { id: 304, name: 'MongoDB日志库', type: 'database' }
  ]
}

// 表单验证规则
const rules: FormRules = {
  monitorType: [
    { required: true, message: '请选择监控类型', trigger: 'change' }
  ],
  targetType: [
    { required: true, message: '请选择目标类型', trigger: 'change' }
  ],
  targetId: [
    { required: true, message: '请选择监控目标', trigger: 'change' }
  ],
  targetName: [
    { required: true, message: '请输入监控名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  metricUnit: [
    { required: true, message: '请输入指标单位', trigger: 'blur' },
    { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
  ],
  warningThreshold: [
    { required: true, message: '请输入警告阈值', trigger: 'blur' },
    { type: 'number', min: 0, max: 100, message: '阈值范围 0-100', trigger: 'blur' }
  ],
  criticalThreshold: [
    { required: true, message: '请输入严重阈值', trigger: 'blur' },
    { type: 'number', min: 0, max: 100, message: '阈值范围 0-100', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== null && formData.warningThreshold !== null && value <= formData.warningThreshold) {
          callback(new Error('严重阈值必须大于警告阈值'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  monitorInterval: [
    { required: true, message: '请输入监控间隔', trigger: 'blur' },
    { type: 'number', min: 30, max: 3600, message: '间隔范围 30-3600 秒', trigger: 'blur' }
  ]
}

// 计算属性
const dialogTitle = computed(() => {
  return props.mode === 'add' ? '添加监控配置' : '编辑监控配置'
})

// 方法定义
const handleMonitorTypeChange = (value: string) => {
  // 根据监控类型设置默认单位
  const unitMap: Record<string, string> = {
    'cpu': '%',
    'memory': '%',
    'disk': '%',
    'network': 'MB/s',
    'gpu': '%',
    'service': '%',
    'database': '个',
    'cache': '%'
  }
  
  if (unitMap[value]) {
    formData.metricUnit = unitMap[value]
  }
}

const handleTargetTypeChange = (value: string) => {
  // 清空目标选择
  formData.targetId = null
  
  // 根据目标类型加载目标选项
  if (mockTargets[value]) {
    targetOptions.value = mockTargets[value]
  } else {
    targetOptions.value = []
  }
}

const getThresholdUnit = () => {
  return formData.metricUnit || '%'
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    const valid = await formRef.value.validate()
    if (!valid) return
    
    loading.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(props.mode === 'add' ? '添加成功' : '保存成功')
    emit('success')
    handleClose()
    
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    loading.value = false
  }
}

const handleClose = () => {
  visible.value = false
  resetForm()
}

const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(formData, {
    monitorType: '',
    targetType: '',
    targetId: null,
    targetName: '',
    metricUnit: '',
    warningThreshold: null,
    criticalThreshold: null,
    monitorInterval: 60,
    enabled: true,
    description: ''
  })
  targetOptions.value = []
}

const initFormData = () => {
  if (props.data) {
    Object.assign(formData, props.data)
    
    // 根据目标类型加载目标选项
    if (formData.targetType && mockTargets[formData.targetType]) {
      targetOptions.value = mockTargets[formData.targetType]
    }
  }
}

// 监听数据变化
watch(() => props.data, (newData) => {
  if (newData) {
    nextTick(() => {
      initFormData()
    })
  }
}, { immediate: true })

watch(() => props.modelValue, (newValue) => {
  if (newValue && props.data) {
    nextTick(() => {
      initFormData()
    })
  }
})
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 20px 20px 15px;
  margin: 0;
}

:deep(.el-dialog__body) {
  padding: 20px;
}

:deep(.el-dialog__footer) {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 15px 20px 20px;
}

:deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.8);
}

:deep(.el-input__inner) {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
}

:deep(.el-textarea__inner) {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
  resize: vertical;
}

:deep(.el-select .el-input__inner) {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
}

:deep(.el-switch__core) {
  background: rgba(255, 255, 255, 0.1);
}

:deep(.el-switch.is-checked .el-switch__core) {
  background: #409eff;
}
</style>