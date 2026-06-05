<template>
  <div class="space-y-6">
    <!-- 欢迎横幅 -->
    <div class="rounded-2xl p-8 text-white shadow-lg"
         style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
      <h2 class="text-3xl font-bold mb-2">欢迎回来, {{ userStore.username }} 👋</h2>
      <p class="text-blue-100">今天想做什么? 管理角色 / 问 AI / 探索鸣潮的世界</p>
    </div>

    <!-- 功能卡片网格 -->
    <el-row :gutter="24">
      <el-col :span="8">
        <div class="rounded-2xl bg-white p-6 cursor-pointer hover:shadow-xl transition-all hover:-translate-y-1 border border-gray-100"
             @click="$router.push('/resonators')">
          <div class="w-14 h-14 rounded-xl flex items-center justify-center mb-4"
               style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
            <el-icon size="28" color="white"><Avatar /></el-icon>
          </div>
          <h3 class="text-xl font-bold text-gray-800 mb-1">我的角色</h3>
          <p class="text-gray-500 text-sm">管理你拥有的鸣潮共鸣者</p>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="rounded-2xl bg-white p-6 cursor-pointer hover:shadow-xl transition-all hover:-translate-y-1 border border-gray-100"
             @click="$router.push('/ai')">
          <div class="w-14 h-14 rounded-xl flex items-center justify-center mb-4"
               style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
            <el-icon size="28" color="white"><ChatDotRound /></el-icon>
          </div>
          <h3 class="text-xl font-bold text-gray-800 mb-1">AI 助手</h3>
          <p class="text-gray-500 text-sm">问我任何关于鸣潮的事</p>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="rounded-2xl bg-white p-6 border border-gray-100 opacity-70">
          <div class="w-14 h-14 rounded-xl flex items-center justify-center mb-4"
               style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
            <el-icon size="28" color="white"><DataLine /></el-icon>
          </div>
          <h3 class="text-xl font-bold text-gray-800 mb-1">数据统计</h3>
          <p class="text-gray-500 text-sm">敬请期待</p>
        </div>
      </el-col>
    </el-row>

    <!-- 功能介绍 -->
    <div class="rounded-2xl bg-white p-6 shadow-sm">
      <h3 class="text-lg font-bold mb-4 flex items-center gap-2">
        <el-icon><Star /></el-icon> AI 助手能做什么
      </h3>
      <div class="grid grid-cols-2 gap-4">
        <div class="flex gap-3 p-3 rounded-lg hover:bg-gray-50">
          <div class="text-2xl">📚</div>
          <div>
            <div class="font-semibold">鸣潮百科</div>
            <div class="text-sm text-gray-500">角色技能 / 世界观 / 攻略</div>
          </div>
        </div>
        <div class="flex gap-3 p-3 rounded-lg hover:bg-gray-50">
          <div class="text-2xl">🎯</div>
          <div>
            <div class="font-semibold">个性化建议</div>
            <div class="text-sm text-gray-500">基于你拥有的角色给建议</div>
          </div>
        </div>
        <div class="flex gap-3 p-3 rounded-lg hover:bg-gray-50">
          <div class="text-2xl">🔍</div>
          <div>
            <div class="font-semibold">实时搜索</div>
            <div class="text-sm text-gray-500">最新新闻 / 资讯</div>
          </div>
        </div>
        <div class="flex gap-3 p-3 rounded-lg hover:bg-gray-50">
          <div class="text-2xl">💻</div>
          <div>
            <div class="font-semibold">GitHub 查询</div>
            <div class="text-sm text-gray-500">开源项目信息</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Avatar, ChatDotRound, DataLine, Star } from '@element-plus/icons-vue'
import { useUserStore } from '../store/user'
const userStore = useUserStore()
</script>
