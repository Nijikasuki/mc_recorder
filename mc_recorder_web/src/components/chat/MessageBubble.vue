<template>
  <div class="flex gap-3" :class="isUser ? 'justify-end' : 'justify-start'">
    <!-- AI 头像 -->
    <div
      v-if="!isUser"
      class="flex h-7 w-7 flex-shrink-0 items-center justify-center rounded-lg bg-gradient-to-br from-cyan-400 to-blue-500 shadow-md shadow-blue-500/20"
    >
      <svg class="h-3.5 w-3.5 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M9.5 3l1.5 4 4 1.5-4 1.5-1.5 4-1.5-4L4 8.5l4-1.5L9.5 3z" />
      </svg>
    </div>

    <!-- 消息气泡 -->
    <div
      class="max-w-[75%] rounded-2xl px-4 py-2.5 text-[14px] leading-relaxed transition-all duration-200"
      :class="isUser
        ? 'bg-gradient-to-br from-amber-400 to-yellow-500 text-[#0D1117] font-medium shadow-lg shadow-amber-500/20'
        : 'bg-[#161B22] text-[#E6E6E8] border border-[#1F242C] hover:border-[#2A3038]'"
    >
      <pre class="whitespace-pre-wrap break-words font-sans">{{ msg.content }}</pre>
    </div>

    <!-- 用户头像 -->
    <div
      v-if="isUser"
      class="flex h-7 w-7 flex-shrink-0 items-center justify-center rounded-lg bg-gradient-to-br from-rose-400 to-pink-500 text-[12px] font-semibold text-white shadow-md shadow-pink-500/20"
    >
      {{ userInitial }}
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  msg: { type: Object, required: true },
  username: { type: String, default: '' },
})

const isUser = computed(() => props.msg.role === 'user')
const userInitial = computed(() => props.username?.charAt(0)?.toUpperCase() || '?')
</script>
