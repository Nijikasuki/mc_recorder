<template>
  <div class="flex gap-3">
    <!-- AI 头像 (脉冲) -->
    <div class="relative flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-blue-500 to-purple-500 text-base shadow-lg shadow-purple-500/30">
      🤖
      <span class="absolute inset-0 rounded-xl bg-purple-400/30 animate-ping"></span>
    </div>

    <!-- 思考气泡 -->
    <div class="rounded-2xl border border-white/10 bg-white/[0.05] px-4 py-3 backdrop-blur-xl">
      <div class="flex items-center gap-3">
        <!-- 三点律动 -->
        <div class="flex gap-1">
          <span class="h-1.5 w-1.5 animate-bounce rounded-full bg-blue-400" style="animation-delay: 0ms"></span>
          <span class="h-1.5 w-1.5 animate-bounce rounded-full bg-purple-400" style="animation-delay: 150ms"></span>
          <span class="h-1.5 w-1.5 animate-bounce rounded-full bg-pink-400" style="animation-delay: 300ms"></span>
        </div>
        <!-- 文字 -->
        <span class="text-sm text-gray-300">{{ thinkingText }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  nodeName: { type: String, default: '' },
})

const TEXT_MAP = {
  retrieve_rag: '🔍 正在检索鸣潮百科',
  llm: '🧠 正在思考',
  tools: '🔧 正在调用工具',
}

const thinkingText = computed(() => TEXT_MAP[props.nodeName] || '思考中')
</script>
