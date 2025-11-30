<template>
  <div id="app">
    <TechLayout>
      <router-view />
    </TechLayout>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import TechLayout from '@/components/layout/TechLayout.vue'

const router = useRouter()

onMounted(() => {
  // 移除加载动画
  const loadingElement = document.querySelector('.loading-container')
  if (loadingElement) {
    loadingElement.remove()
  }
})
</script>

<style>
@import '@/styles/tech-theme.css';

#app {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #0f111a 0%, #1a1d2e 100%);
  overflow: hidden;
}
</style>