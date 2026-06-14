<template>
  <div class="flex min-h-screen flex-col bg-gray-50 text-gray-900 antialiased">
    <!-- 顶部 nav -->
    <header class="sticky top-0 z-30 border-b border-gray-200/80 bg-white/80 backdrop-blur-xl">
      <div class="flex h-14 items-center justify-between px-6">
        <!-- Logo -->
        <button @click="$router.push('/home')" class="group flex items-center gap-2.5 transition-opacity hover:opacity-80">
          <div class="flex h-7 w-7 items-center justify-center rounded-lg bg-gradient-to-br from-indigo-500 to-violet-500 text-sm shadow-sm">
            <span class="text-white">🎮</span>
          </div>
          <span class="text-[15px] font-semibold tracking-tight text-gray-900">鸣潮记录</span>
        </button>

        <!-- Nav -->
        <nav class="flex items-center gap-1">
          <button
            v-for="item in navItems"
            :key="item.to"
            @click="$router.push(item.to)"
            class="group flex items-center gap-1.5 rounded-lg px-3 py-1.5 text-sm font-medium transition-all duration-200"
            :class="$route.path.startsWith(item.to)
              ? 'bg-gray-100 text-gray-900'
              : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'"
          >
            <span class="text-[15px]">{{ item.icon }}</span>
            <span>{{ item.label }}</span>
          </button>
        </nav>

        <!-- 用户 + 退出 -->
        <div class="flex items-center gap-2">
          <button class="flex items-center gap-2 rounded-lg px-2 py-1 transition-colors hover:bg-gray-100">
            <div class="flex h-6 w-6 items-center justify-center rounded-md bg-gradient-to-br from-indigo-500 to-violet-500 text-[11px] font-semibold text-white">
              {{ userStore.username?.charAt(0)?.toUpperCase() }}
            </div>
            <span class="text-sm font-medium text-gray-700">{{ userStore.username }}</span>
          </button>
          <button
            @click="handleLogout"
            class="rounded-lg p-1.5 text-gray-500 transition-all hover:bg-red-50 hover:text-red-600 active:scale-95"
            title="退出登录"
          >
            <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
          </button>
        </div>
      </div>
    </header>

    <!-- 主体 -->
    <main class="flex-1" :class="isAiPage ? '' : 'mx-auto w-full max-w-7xl px-6 py-8'">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const navItems = [
  { to: '/home', icon: '🏠', label: '主页' },
  { to: '/resonators', icon: '⚔️', label: '我的角色' },
  { to: '/ai', icon: '✨', label: 'AI 助手' },
]

const isAiPage = computed(() => route.path === '/ai')

function handleLogout() {
  userStore.logout()
  ElMessage.success('已退出')
  router.push('/login')
}
</script>

<style>
/* 字体: 系统字体栈 */
html {
  font-family: -apple-system, BlinkMacSystemFont, 'Inter', 'Segoe UI', 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 滚动条美化 (浅色) */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}
::-webkit-scrollbar-track {
  background: transparent;
}
::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.12);
  border-radius: 4px;
  transition: background 0.3s;
}
::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.22);
}

/* 平滑滚动 */
html {
  scroll-behavior: smooth;
}

/* 选中色 (Linear 紫) */
::selection {
  background: rgba(79, 70, 229, 0.18);
  color: inherit;
}
</style>
