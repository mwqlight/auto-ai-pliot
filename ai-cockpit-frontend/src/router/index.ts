import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/user/Login.vue'),
    meta: {
      title: '登录 - AI驾驶舱平台',
      requiresAuth: false
    }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/DashboardView.vue'),
    meta: {
      title: '驾驶舱总览 - AI驾驶舱平台',
      requiresAuth: true
    }
  },
  {
    path: '/model',
    name: 'Model',
    redirect: '/model/warehouse',
    component: () => import('@/views/model/Layout.vue'),
    meta: {
      title: '模型工坊 - AI驾驶舱平台',
      requiresAuth: true
    },
    children: [
      {
        path: 'warehouse',
        name: 'ModelWarehouse',
        component: () => import('@/views/model/Warehouse.vue'),
        meta: { title: '模型仓库' }
      },
      {
        path: 'training',
        name: 'ModelTraining',
        component: () => import('@/views/model/Training.vue'),
        meta: { title: '模型训练' }
      },
      {
        path: 'deployment',
        name: 'ModelDeployment',
        component: () => import('@/views/model/Deployment.vue'),
        meta: { title: '模型部署' }
      }
    ]
  },
  {
    path: '/dataset',
    name: 'Dataset',
    redirect: '/dataset/warehouse',
    component: () => import('@/views/dataset/Layout.vue'),
    meta: {
      title: '数据实验室 - AI驾驶舱平台',
      requiresAuth: true
    },
    children: [
      {
        path: 'warehouse',
        name: 'DatasetWarehouse',
        component: () => import('@/views/dataset/Warehouse.vue'),
        meta: { title: '数据集仓库' }
      },
      {
        path: 'preprocessing',
        name: 'DataPreprocessing',
        component: () => import('@/views/dataset/Preprocessing.vue'),
        meta: { title: '数据预处理' }
      },
      {
        path: 'annotation',
        name: 'DataAnnotation',
        component: () => import('@/views/dataset/Annotation.vue'),
        meta: { title: '数据标注' }
      }
    ]
  },
  {
    path: '/ai-center',
    name: 'AICenter',
    redirect: '/ai-center/rag',
    component: () => import('@/views/ai-center/Layout.vue'),
    meta: {
      title: 'AI技术方案中心 - AI驾驶舱平台',
      requiresAuth: true
    },
    children: [
      {
        path: 'rag',
        name: 'RAGSystem',
        component: () => import('@/views/ai-center/RAGSystem.vue'),
        meta: { title: 'RAG系统' }
      },
      {
        path: 'kag',
        name: 'KAGSystem',
        component: () => import('@/views/ai-center/KAGSystem.vue'),
        meta: { title: 'KAG系统' }
      },
      {
        path: 'agent',
        name: 'AgentSystem',
        component: () => import('@/views/ai-center/AgentSystem.vue'),
        meta: { title: 'Agent系统' }
      }
    ]
  },
  {
    path: '/applications',
    name: 'Applications',
    component: () => import('@/views/ApplicationsView.vue'),
    meta: {
      title: '业务应用市场 - AI驾驶舱平台',
      requiresAuth: true
    }
  },
  {
    path: '/monitor',
    name: 'Monitor',
    redirect: '/monitor/resources',
    component: () => import('@/views/monitor/Layout.vue'),
    meta: {
      title: '资源监控中心 - AI驾驶舱平台',
      requiresAuth: true
    },
    children: [
      {
        path: 'resources',
        name: 'ResourceMonitor',
        component: () => import('@/views/monitor/ResourceMonitor.vue'),
        meta: { title: '资源监控' }
      },
      {
        path: 'performance',
        name: 'PerformanceMonitor',
        component: () => import('@/views/monitor/PerformanceMonitor.vue'),
        meta: { title: '性能监控' }
      },
      {
        path: 'api',
        name: 'APIMonitor',
        component: () => import('@/views/monitor/ApiMonitor.vue'),
        meta: { title: 'API监控' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: {
      title: '页面未找到 - AI驾驶舱平台',
      requiresAuth: false
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title as string
  }
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      next('/login')
      return
    }
  }
  
  next()
})

export default router