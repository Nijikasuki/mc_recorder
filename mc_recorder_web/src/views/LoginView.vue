<template>
  <div class="relative flex min-h-screen items-center justify-center overflow-hidden bg-[#0D1117]">
    <!-- 背景渐变 -->
    <div class="absolute inset-0 bg-gradient-to-br from-[#0F1B2D] via-[#0D1117] to-[#101626]"></div>

    <!-- 数据感网格背景 -->
    <div
      class="pointer-events-none absolute inset-0 opacity-[0.04]"
      style="background-image: linear-gradient(rgba(96, 165, 250, 0.6) 1px, transparent 1px), linear-gradient(90deg, rgba(96, 165, 250, 0.6) 1px, transparent 1px); background-size: 32px 32px;"
    ></div>

    <!-- 装饰光晕 -->
    <div class="pointer-events-none absolute -left-32 top-1/4 h-96 w-96 rounded-full bg-blue-500/15 blur-3xl"></div>
    <div class="pointer-events-none absolute -right-32 bottom-1/4 h-96 w-96 rounded-full bg-cyan-400/10 blur-3xl"></div>
    <div class="pointer-events-none absolute left-1/2 top-1/2 h-72 w-72 -translate-x-1/2 -translate-y-1/2 rounded-full bg-indigo-500/10 blur-3xl"></div>

    <!-- 装饰几何 (左下角和右上角) -->
    <svg class="pointer-events-none absolute -left-8 -top-8 h-40 w-40 text-cyan-400/10" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="0.4">
      <path d="M12 2L20 7v10l-8 5-8-5V7l8-5z" />
      <path d="M12 6L17 9v6l-5 3-5-3V9l5-3z" />
    </svg>
    <svg class="pointer-events-none absolute -bottom-8 -right-8 h-48 w-48 text-blue-400/10" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="0.4">
      <path d="M12 2L20 7v10l-8 5-8-5V7l8-5z" />
    </svg>

    <!-- 登录卡片 (磨砂玻璃) -->
    <div class="relative z-10 w-[420px]">
      <!-- 装饰光环 -->
      <div class="absolute -inset-px rounded-3xl bg-gradient-to-br from-cyan-400/20 via-blue-500/10 to-transparent opacity-50 blur-sm"></div>

      <div class="relative overflow-hidden rounded-3xl border border-white/10 bg-white/[0.03] p-8 shadow-2xl shadow-blue-500/10 backdrop-blur-2xl">
        <!-- 卡片内顶部装饰线 -->
        <div class="absolute left-8 right-8 top-0 h-px bg-gradient-to-r from-transparent via-cyan-400/40 to-transparent"></div>

        <!-- Header -->
        <div class="text-center">
          <!-- Logo 六边形 -->
          <div class="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-400 to-blue-500 shadow-xl shadow-blue-500/40">
            <svg class="h-7 w-7 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M12 2L20 7v10l-8 5-8-5V7l8-5z" />
              <circle cx="12" cy="12" r="3" fill="currentColor" fill-opacity="0.3" stroke="none" />
            </svg>
          </div>

          <!-- 在线状态 -->
          <div class="mb-2 flex items-center justify-center gap-2">
            <span class="relative flex h-1.5 w-1.5">
              <span class="absolute h-full w-full animate-ping rounded-full bg-cyan-400 opacity-70"></span>
              <span class="relative h-1.5 w-1.5 rounded-full bg-cyan-400"></span>
            </span>
            <span class="text-[10px] font-medium uppercase tracking-[0.2em] text-cyan-400">
              SYSTEM ONLINE
            </span>
          </div>

          <h1 class="text-[24px] font-bold text-white">
            鸣潮记录
          </h1>
          <p class="mt-1 text-[12px] text-blue-200/60">你的全能 AI 助手 · Tethys</p>
        </div>

        <!-- 表单 -->
        <form @submit.prevent="handleLogin" class="mt-8 space-y-4">
          <!-- 用户名 -->
          <div>
            <label class="mb-1.5 block text-[11px] font-medium uppercase tracking-wider text-blue-200/60">用户名</label>
            <div class="group relative">
              <div class="pointer-events-none absolute inset-y-0 left-3 flex items-center text-blue-200/40 transition-colors group-focus-within:text-cyan-400">
                <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.8">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
              </div>
              <input
                v-model="form.username"
                type="text"
                placeholder="请输入用户名"
                class="w-full rounded-xl border border-white/10 bg-[#0D1117]/50 py-2.5 pl-10 pr-3 text-[14px] text-white placeholder-blue-200/30 outline-none transition-all duration-200 backdrop-blur focus:border-cyan-400/50 focus:bg-[#0D1117]/80 focus:shadow-lg focus:shadow-cyan-500/10"
              />
            </div>
            <p v-if="errors.username" class="mt-1 text-[11px] text-rose-400">{{ errors.username }}</p>
          </div>

          <!-- 密码 -->
          <div>
            <label class="mb-1.5 block text-[11px] font-medium uppercase tracking-wider text-blue-200/60">密码</label>
            <div class="group relative">
              <div class="pointer-events-none absolute inset-y-0 left-3 flex items-center text-blue-200/40 transition-colors group-focus-within:text-cyan-400">
                <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.8">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 11c1.5 0 2.5-1 2.5-2.5S13.5 6 12 6s-2.5 1-2.5 2.5S10.5 11 12 11zm0 0v4M6 21h12a2 2 0 002-2v-7a2 2 0 00-2-2H6a2 2 0 00-2 2v7a2 2 0 002 2z" />
                </svg>
              </div>
              <input
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="请输入密码"
                class="w-full rounded-xl border border-white/10 bg-[#0D1117]/50 py-2.5 pl-10 pr-10 text-[14px] text-white placeholder-blue-200/30 outline-none transition-all duration-200 backdrop-blur focus:border-cyan-400/50 focus:bg-[#0D1117]/80 focus:shadow-lg focus:shadow-cyan-500/10"
              />
              <button
                type="button"
                @click="showPassword = !showPassword"
                class="absolute inset-y-0 right-3 flex items-center text-blue-200/40 transition-colors hover:text-cyan-400"
                tabindex="-1"
              >
                <svg v-if="!showPassword" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.8">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" d="M2.46 12C3.73 7.94 7.52 5 12 5c4.48 0 8.27 2.94 9.54 7-1.27 4.06-5.06 7-9.54 7-4.48 0-8.27-2.94-9.54-7z" />
                </svg>
                <svg v-else class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.8">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.48 0-8.27-2.94-9.54-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88L3 3m6.88 6.88l4.24 4.24m0 0L21 21" />
                </svg>
              </button>
            </div>
            <p v-if="errors.password" class="mt-1 text-[11px] text-rose-400">{{ errors.password }}</p>
          </div>

          <!-- 登录按钮 -->
          <button
            type="submit"
            :disabled="loading"
            class="group relative mt-2 flex w-full items-center justify-center gap-2 overflow-hidden rounded-xl bg-gradient-to-r from-cyan-400 to-blue-500 py-2.5 text-[14px] font-semibold text-white shadow-lg shadow-blue-500/30 transition-all duration-200 hover:shadow-xl hover:shadow-blue-500/50 hover:brightness-110 active:scale-[0.98] disabled:cursor-not-allowed disabled:opacity-60"
          >
            <span v-if="!loading" class="flex items-center gap-2">
              登 录
              <svg class="h-4 w-4 transition-transform duration-200 group-hover:translate-x-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M5 12h14M13 5l7 7-7 7" />
              </svg>
            </span>
            <span v-else class="flex items-center gap-2">
              <svg class="h-4 w-4 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="3" stroke-opacity="0.25"></circle>
                <path fill="currentColor" d="M4 12a8 8 0 018-8v3a5 5 0 00-5 5H4z"></path>
              </svg>
              登录中...
            </span>
          </button>

          <!-- 注册链接 -->
          <div class="text-center text-[12px] text-blue-200/50">
            还没账号?
            <a
              @click="router.push('/register')"
              class="cursor-pointer font-medium text-cyan-400 transition-colors hover:text-cyan-300 hover:underline"
            >
              立即注册
            </a>
          </div>
        </form>

        <!-- 卡片底部装饰线 -->
        <div class="absolute bottom-0 left-8 right-8 h-px bg-gradient-to-r from-transparent via-cyan-400/20 to-transparent"></div>
      </div>

      <!-- 卡片下方版本号 -->
      <div class="mt-4 text-center">
        <p class="text-[10px] font-medium uppercase tracking-[0.2em] text-blue-200/30">
          MC Recorder v2.0 · WUTHERING WAVES TOOL
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api/auth'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const showPassword = ref(false)

const form = reactive({
  username: '',
  password: '',
})

const errors = reactive({
  username: '',
  password: '',
})

function validate() {
  errors.username = form.username.trim() ? '' : '请输入用户名'
  errors.password = form.password.trim() ? '' : '请输入密码'
  return !errors.username && !errors.password
}

async function handleLogin() {
  if (!validate()) return

  loading.value = true
  try {
    const res = await login(form.username, form.password)
    if (res.code === 0) {
      userStore.setLogin(res.data, form.username)
      ElMessage.success('登录成功')
      router.push('/home')
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } catch (err) {
    ElMessage.error(err.response?.data?.message || '网络错误')
  } finally {
    loading.value = false
  }
}
</script>
