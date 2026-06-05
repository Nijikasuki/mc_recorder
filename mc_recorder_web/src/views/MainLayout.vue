<template>
  <el-container class="min-h-screen">
    <el-header class="bg-white shadow-sm flex items-center justify-between px-6" style="height: 64px;">
      <div class="flex items-center gap-3">
        <div class="text-2xl">🎮</div>
        <h1 class="text-lg font-bold"
            style="background: linear-gradient(90deg, #667eea, #764ba2); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
          鸣潮记录工具
        </h1>
      </div>

      <el-menu mode="horizontal" :default-active="$route.path" router
               class="border-none flex-1 justify-center" style="background: transparent;">
        <el-menu-item index="/home">
          <el-icon><HomeFilled /></el-icon>
          <span>主页</span>
        </el-menu-item>
        <el-menu-item index="/resonators">
          <el-icon><Avatar /></el-icon>
          <span>我的角色</span>
        </el-menu-item>
        <el-menu-item index="/ai">
          <el-icon><ChatDotRound /></el-icon>
          <span>AI 助手</span>
        </el-menu-item>
      </el-menu>

      <div class="flex items-center gap-3">
        <el-avatar size="small" style="background: linear-gradient(135deg, #667eea, #764ba2);">
          {{ userStore.username?.charAt(0)?.toUpperCase() }}
        </el-avatar>
        <span class="text-gray-700 font-medium">{{ userStore.username }}</span>
        <el-button size="small" :icon="SwitchButton" @click="handleLogout">退出</el-button>
      </div>
    </el-header>

    <el-main class="bg-gray-50" style="padding: 24px;">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { HomeFilled, Avatar, ChatDotRound, SwitchButton } from '@element-plus/icons-vue'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()

function handleLogout() {
  userStore.logout()
  ElMessage.success('已退出')
  router.push('/login')
}
</script>
