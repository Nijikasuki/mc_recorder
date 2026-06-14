<template>
  <div class="flex min-h-screen flex-col bg-[#0D1117] text-[#E6E6E8] antialiased">
    <!-- 顶部 nav -->
    <header class="sticky top-0 z-30 border-b border-[#1F242C] bg-[#0D1117]/95 backdrop-blur-xl">
      <div class="flex h-14 items-center justify-between px-6">
        <!-- Logo -->
        <button @click="$router.push('/home')" class="group flex items-center gap-2.5 transition-opacity hover:opacity-90">
          <div class="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-amber-400 to-yellow-500 text-base shadow-lg shadow-amber-500/20">
            <svg class="h-4 w-4 text-[#0D1117]" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2L20 7v10l-8 5-8-5V7l8-5z" />
            </svg>
          </div>
          <span class="text-[15px] font-semibold tracking-tight text-[#E6E6E8]">鸣潮记录</span>
          <span class="rounded-md bg-amber-400/10 px-1.5 py-0.5 text-[10px] font-medium text-amber-400">v2.0</span>
        </button>

        <!-- Nav -->
        <nav class="flex items-center gap-1">
          <button
            v-for="item in navItems"
            :key="item.to"
            @click="$router.push(item.to)"
            class="group flex items-center gap-2 rounded-lg px-3 py-1.5 text-sm font-medium transition-all duration-200"
            :class="$route.path.startsWith(item.to)
              ? 'bg-[#1F242C] text-[#E6E6E8] shadow-sm'
              : 'text-[#8B949E] hover:bg-[#161B22] hover:text-[#E6E6E8]'"
          >
            <component :is="item.icon" class="h-4 w-4" />
            <span>{{ item.label }}</span>
          </button>
        </nav>

        <!-- 用户 + 退出 -->
        <div class="flex items-center gap-2">
          <div class="flex items-center gap-2 rounded-lg bg-[#161B22] px-2.5 py-1">
            <div class="flex h-6 w-6 items-center justify-center rounded-md bg-gradient-to-br from-rose-400 to-pink-500 text-[11px] font-semibold text-white">
              {{ userStore.username?.charAt(0)?.toUpperCase() }}
            </div>
            <span class="text-[13px] font-medium text-[#E6E6E8]">{{ userStore.username }}</span>
          </div>
          <button
            @click="handleLogout"
            class="flex h-8 w-8 items-center justify-center rounded-lg text-[#8B949E] transition-all hover:bg-rose-500/10 hover:text-rose-400 active:scale-95"
            title="退出"
          >
            <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.8">
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
import { computed, h } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const IconHome = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M3 12l9-9 9 9M5 10v10h14V10' }),
])
const IconResonator = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M12 2L20 7v10l-8 5-8-5V7l8-5z' }),
  h('circle', { cx: '12', cy: '12', r: '3' }),
])
const IconAi = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M9.5 3l1.5 4 4 1.5-4 1.5-1.5 4-1.5-4L4 8.5l4-1.5L9.5 3zm7 8l1 2.5 2.5 1-2.5 1-1 2.5-1-2.5L13 14.5l2.5-1 1-2.5z' }),
])

const navItems = [
  { to: '/home', icon: IconHome, label: '首页' },
  { to: '/resonators', icon: IconResonator, label: '我的角色' },
  { to: '/ai', icon: IconAi, label: 'AI 助手' },
]

const isAiPage = computed(() => route.path === '/ai')

function handleLogout() {
  userStore.logout()
  ElMessage.success('已退出')
  router.push('/login')
}
</script>

<style>
html {
  font-family: -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Microsoft YaHei',
    'Inter', 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
  background: #0D1117;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

body {
  background: #0D1117;
  color: #E6E6E8;
}

::-webkit-scrollbar { width: 8px; height: 8px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.08);
  border-radius: 4px;
  transition: background 0.3s;
}
::-webkit-scrollbar-thumb:hover { background: rgba(255, 255, 255, 0.18); }

html { scroll-behavior: smooth; }

::selection {
  background: rgba(251, 191, 36, 0.25);
  color: #E6E6E8;
}

/* ============================================================
 * Element Plus 暗色覆盖 (table / dialog / form / input)
 * ============================================================ */

/* el-table */
.dark-table .el-table {
  --el-table-bg-color: #161B22;
  --el-table-tr-bg-color: #161B22;
  --el-table-header-bg-color: #0D1117;
  --el-table-border-color: #1F242C;
  --el-table-text-color: #E6E6E8;
  --el-table-header-text-color: #8B949E;
  --el-table-row-hover-bg-color: #1F242C;
  --el-fill-color-blank: #161B22;
  color: #E6E6E8;
}

.dark-table .el-table th.el-table__cell {
  background-color: #0D1117 !important;
  color: #8B949E;
  font-weight: 500;
  border-bottom-color: #1F242C !important;
}

.dark-table .el-table td.el-table__cell {
  border-bottom-color: #1F242C !important;
}

.dark-table .el-table--enable-row-hover .el-table__body tr:hover > td.el-table__cell {
  background-color: #1F242C !important;
}

.dark-table .el-table__empty-block {
  background-color: #161B22;
}
.dark-table .el-table__empty-text {
  color: #484F58;
}

/* el-dialog */
.dark-dialog {
  --el-dialog-bg-color: #161B22;
}
.dark-dialog .el-dialog__header {
  background: #0D1117;
  border-bottom: 1px solid #1F242C;
  margin-right: 0;
  padding: 16px 20px;
}
.dark-dialog .el-dialog__title {
  color: #E6E6E8;
  font-size: 15px;
  font-weight: 600;
}
.dark-dialog .el-dialog__body {
  background: #161B22;
  color: #E6E6E8;
  padding: 20px;
}
.dark-dialog .el-dialog__footer {
  background: #161B22;
  border-top: 1px solid #1F242C;
  padding: 12px 20px;
}
.dark-dialog .el-dialog__headerbtn .el-dialog__close {
  color: #8B949E;
}
.dark-dialog .el-dialog__headerbtn:hover .el-dialog__close {
  color: #E6E6E8;
}

/* form 内的 input / select / number 等 */
.dark-dialog .el-form-item__label,
.dark-dialog .el-collapse-item__header {
  color: #8B949E !important;
}
.dark-dialog .el-input__wrapper,
.dark-dialog .el-textarea__inner {
  background-color: #0D1117 !important;
  box-shadow: 0 0 0 1px #1F242C inset !important;
}
.dark-dialog .el-input__wrapper:hover,
.dark-dialog .el-input__wrapper.is-focus {
  box-shadow: 0 0 0 1px #2A3038 inset !important;
}
.dark-dialog .el-input__inner {
  color: #E6E6E8;
}
.dark-dialog .el-input__inner::placeholder {
  color: #484F58;
}
.dark-dialog .el-collapse,
.dark-dialog .el-collapse-item__wrap,
.dark-dialog .el-collapse-item__header {
  background-color: transparent !important;
  border-color: #1F242C !important;
  color: #E6E6E8 !important;
}

/* el-loading 覆盖深色 */
.el-loading-mask {
  background-color: rgba(13, 17, 23, 0.7) !important;
}
.el-loading-spinner .path {
  stroke: #C9A961;
}
</style>
