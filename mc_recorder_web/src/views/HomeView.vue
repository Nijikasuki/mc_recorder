<template>
  <div class="space-y-6">
    <!-- 欢迎横幅 -->
    <div class="relative overflow-hidden rounded-2xl border border-[#1F242C] bg-gradient-to-br from-[#0F1B2D] via-[#101626] to-[#0D1117] p-8">
      <!-- 数据感网格背景 -->
      <div
        class="pointer-events-none absolute inset-0 opacity-[0.05]"
        style="background-image: linear-gradient(rgba(96, 165, 250, 0.6) 1px, transparent 1px), linear-gradient(90deg, rgba(96, 165, 250, 0.6) 1px, transparent 1px); background-size: 28px 28px;"
      ></div>
      <!-- 蓝色光晕 -->
      <div class="pointer-events-none absolute -right-12 -top-12 h-56 w-56 rounded-full bg-blue-500/15 blur-3xl"></div>
      <div class="pointer-events-none absolute -bottom-16 left-1/3 h-40 w-40 rounded-full bg-cyan-400/10 blur-3xl"></div>

      <div class="relative z-10 flex items-center justify-between">
        <div>
          <!-- 在线状态 -->
          <div class="flex items-center gap-2">
            <span class="relative flex h-2 w-2">
              <span class="absolute h-full w-full animate-ping rounded-full bg-cyan-400 opacity-70"></span>
              <span class="relative h-2 w-2 rounded-full bg-cyan-400"></span>
            </span>
            <span class="text-[10px] font-medium uppercase tracking-[0.2em] text-cyan-400">
              SYSTEM ONLINE
            </span>
          </div>

          <h2 class="mt-3 text-[28px] font-bold text-white">
            欢迎回来, {{ userStore.username }}
          </h2>
          <p class="mt-1.5 text-[13px] text-blue-200/70">
            今天想做什么? 管理角色 / 问 AI / 探索鸣潮的世界
          </p>

          <!-- 状态行 -->
          <div class="mt-4 flex items-center gap-4 text-[11px] text-blue-200/60">
            <div class="flex items-center gap-1.5">
              <span class="h-1 w-1 rounded-full bg-cyan-400"></span>
              <span>当前时间</span>
              <span class="font-mono text-cyan-300">{{ currentTime }}</span>
            </div>
            <span class="h-3 w-px bg-blue-200/20"></span>
            <div class="flex items-center gap-1.5">
              <span class="h-1 w-1 rounded-full bg-emerald-400"></span>
              <span>所有服务正常</span>
            </div>
          </div>
        </div>

        <!-- 装饰几何 (鸣潮气质 - 六边形) -->
        <div class="hidden md:block">
          <svg class="h-24 w-24 text-cyan-400/25" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="0.6">
            <path d="M12 2L20 7v10l-8 5-8-5V7l8-5z" />
            <path d="M12 6L17 9v6l-5 3-5-3V9l5-3z" />
            <circle cx="12" cy="12" r="1.5" fill="currentColor" fill-opacity="0.5" stroke="none" />
          </svg>
        </div>
      </div>
    </div>

    <!-- 功能卡片网格 -->
    <div class="grid grid-cols-1 gap-4 md:grid-cols-3">
      <button
        @click="$router.push('/resonators')"
        class="group relative overflow-hidden rounded-2xl border border-[#1F242C] bg-[#161B22] p-6 text-left transition-all duration-200 hover:-translate-y-1 hover:border-[#2A3038] hover:bg-[#1F242C]"
      >
        <div class="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-rose-400 to-pink-500 shadow-lg shadow-pink-500/20">
          <svg class="h-6 w-6 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 2L20 7v10l-8 5-8-5V7l8-5z" />
            <circle cx="12" cy="12" r="3" stroke-linecap="round" />
          </svg>
        </div>
        <h3 class="text-[16px] font-semibold text-[#E6E6E8]">我的角色</h3>
        <p class="mt-1 text-[12px] text-[#8B949E]">管理你拥有的鸣潮共鸣者</p>
      </button>

      <button
        @click="$router.push('/ai')"
        class="group relative overflow-hidden rounded-2xl border border-[#1F242C] bg-[#161B22] p-6 text-left transition-all duration-200 hover:-translate-y-1 hover:border-[#2A3038] hover:bg-[#1F242C]"
      >
        <div class="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-cyan-400 to-blue-500 shadow-lg shadow-blue-500/20">
          <svg class="h-6 w-6 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9.5 3l1.5 4 4 1.5-4 1.5-1.5 4-1.5-4L4 8.5l4-1.5L9.5 3zm7 8l1 2.5 2.5 1-2.5 1-1 2.5-1-2.5L13 14.5l2.5-1 1-2.5z" />
          </svg>
        </div>
        <h3 class="text-[16px] font-semibold text-[#E6E6E8]">AI 助手</h3>
        <p class="mt-1 text-[12px] text-[#8B949E]">问我任何关于鸣潮的事</p>
      </button>

      <div class="relative overflow-hidden rounded-2xl border border-[#1F242C] bg-[#161B22] p-6 opacity-60">
        <div class="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-amber-400 to-orange-500 shadow-lg shadow-orange-500/20">
          <svg class="h-6 w-6 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <path stroke-linecap="round" stroke-linejoin="round" d="M3 21h18M5 21V8l7-5 7 5v13M9 21V12h6v9" />
          </svg>
        </div>
        <h3 class="text-[16px] font-semibold text-[#E6E6E8]">数据统计</h3>
        <p class="mt-1 text-[12px] text-[#8B949E]">敬请期待</p>
      </div>
    </div>

    <!-- AI 助手能做什么 -->
    <div class="rounded-2xl border border-[#1F242C] bg-[#161B22] p-6">
      <h3 class="mb-5 flex items-center gap-2 text-[15px] font-semibold text-[#E6E6E8]">
        <svg class="h-4 w-4 text-amber-400" viewBox="0 0 24 24" fill="currentColor">
          <path d="M12 2l2.4 7.4H22l-6.3 4.6 2.4 7.4L12 16.8l-6.1 4.6 2.4-7.4L2 9.4h7.6L12 2z" />
        </svg>
        AI 助手能做什么
      </h3>
      <div class="grid grid-cols-1 gap-3 sm:grid-cols-2">
        <div
          v-for="(item, idx) in features"
          :key="item.title"
          class="group flex gap-3 rounded-xl border border-transparent p-3 transition-all duration-200 hover:border-[#1F242C] hover:bg-[#0D1117]"
        >
          <div class="flex h-9 w-9 flex-shrink-0 items-center justify-center rounded-lg" :class="featureColors[idx]">
            <component :is="item.icon" class="h-4 w-4" />
          </div>
          <div>
            <div class="text-[13px] font-medium text-[#E6E6E8]">{{ item.title }}</div>
            <div class="mt-0.5 text-[11px] text-[#8B949E]">{{ item.desc }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { h, ref, onMounted, onBeforeUnmount } from 'vue'
import { useUserStore } from '../store/user'
const userStore = useUserStore()

// 时间显示 (装饰用, 每秒更新)
const currentTime = ref('')
function updateTime() {
  const d = new Date()
  const pad = (n) => String(n).padStart(2, '0')
  currentTime.value = `${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}
let timer
onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
})
onBeforeUnmount(() => clearInterval(timer))

const Book = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M4 4h12a4 4 0 014 4v12H8a4 4 0 01-4-4V4z' }),
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M4 4v16M8 8h8M8 12h8' }),
])
const Target = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('circle', { cx: '12', cy: '12', r: '9' }),
  h('circle', { cx: '12', cy: '12', r: '5' }),
  h('circle', { cx: '12', cy: '12', r: '1.5', fill: 'currentColor', stroke: 'none' }),
])
const Search = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('circle', { cx: '11', cy: '11', r: '7' }),
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M21 21l-4.5-4.5' }),
])
const Code = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M8 9l-4 3 4 3M16 9l4 3-4 3M14 5l-4 14' }),
])

const features = [
  { title: '鸣潮百科', desc: '角色技能 / 世界观 / 攻略', icon: Book },
  { title: '个性化建议', desc: '基于你拥有的角色给建议', icon: Target },
  { title: '实时搜索', desc: '最新新闻 / 资讯', icon: Search },
  { title: 'GitHub 查询', desc: '开源项目信息', icon: Code },
]

const featureColors = [
  'bg-amber-500/15 text-amber-400',
  'bg-rose-500/15 text-rose-400',
  'bg-blue-500/15 text-blue-400',
  'bg-emerald-500/15 text-emerald-400',
]
</script>
