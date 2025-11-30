<template>
  <div class="login-container">
    <!-- 背景特效 -->
    <div class="background-effects">
      <div class="particles"></div>
      <div class="glow-effect"></div>
    </div>
    
    <!-- 登录表单 -->
    <div class="login-form">
      <div class="form-header">
        <h1 class="title">AI驾驶舱平台</h1>
        <p class="subtitle">智能模型管理与应用平台</p>
      </div>
      
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="loginRules" 
        class="form-content"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="form-footer">
        <p class="tip">还没有账号？<router-link to="/register" class="link">立即注册</router-link></p>
      </div>
    </div>
    
    <!-- 版权信息 -->
    <div class="copyright">
      <p>© 2024 AI驾驶舱平台 版权所有</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  const valid = await loginFormRef.value.validate()
  if (!valid) return
  
  loading.value = true
  
  try {
    await userStore.login(loginForm)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    ElMessage.error('登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 如果已登录，直接跳转到首页
  if (userStore.isLoggedIn()) {
    router.push('/dashboard')
  }
})
</script>

<style scoped lang="scss">
.login-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #0f111a 0%, #1a1d2e 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.background-effects {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  
  .particles {
    position: absolute;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle at 20% 50%, rgba(64, 158, 255, 0.1) 0%, transparent 50%);
    animation: particles 20s infinite linear;
  }
  
  .glow-effect {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 300px;
    height: 300px;
    background: radial-gradient(circle, rgba(64, 158, 255, 0.2) 0%, transparent 70%);
    transform: translate(-50%, -50%);
    filter: blur(40px);
    animation: glow 6s ease-in-out infinite alternate;
  }
}

.login-form {
  position: relative;
  z-index: 10;
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
  
  .title {
    font-size: 28px;
    font-weight: 600;
    color: #fff;
    margin-bottom: 8px;
    background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
  
  .subtitle {
    font-size: 14px;
    color: rgba(255, 255, 255, 0.7);
  }
}

.form-content {
  .el-form-item {
    margin-bottom: 24px;
  }
  
  .el-input {
    :deep(.el-input__wrapper) {
      background: rgba(255, 255, 255, 0.1);
      border: 1px solid rgba(255, 255, 255, 0.2);
      border-radius: 8px;
      box-shadow: none;
      
      &:hover {
        border-color: #409EFF;
      }
      
      &.is-focus {
        border-color: #409EFF;
        box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
      }
    }
    
    :deep(.el-input__inner) {
      color: #fff;
      
      &::placeholder {
        color: rgba(255, 255, 255, 0.5);
      }
    }
    
    :deep(.el-input__prefix) {
      color: rgba(255, 255, 255, 0.7);
    }
  }
  
  .login-btn {
    width: 100%;
    height: 48px;
    font-size: 16px;
    background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
    border: none;
    border-radius: 8px;
    
    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 8px 20px rgba(64, 158, 255, 0.3);
    }
  }
}

.form-footer {
  text-align: center;
  margin-top: 20px;
  
  .tip {
    color: rgba(255, 255, 255, 0.7);
    font-size: 14px;
  }
  
  .link {
    color: #409EFF;
    text-decoration: none;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

.copyright {
  position: absolute;
  bottom: 20px;
  left: 0;
  width: 100%;
  text-align: center;
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
}

@keyframes particles {
  0% {
    transform: translateY(0) rotate(0deg);
  }
  100% {
    transform: translateY(-100px) rotate(360deg);
  }
}

@keyframes glow {
  0% {
    opacity: 0.3;
    transform: translate(-50%, -50%) scale(1);
  }
  100% {
    opacity: 0.6;
    transform: translate(-50%, -50%) scale(1.2);
  }
}

@media (max-width: 480px) {
  .login-form {
    width: 90%;
    padding: 30px 20px;
  }
}
</style>