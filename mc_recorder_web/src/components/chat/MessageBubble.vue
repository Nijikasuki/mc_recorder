<template>
  <div class="flex gap-3" :class="isUser ? 'justify-end' : 'justify-start'">
    <!-- AI 头像 -->
    <div
      v-if="!isUser"
      class="flex h-7 w-7 flex-shrink-0 items-center justify-center rounded-lg bg-gradient-to-br from-indigo-500 to-violet-500 text-sm shadow-sm"
    >
      <span class="text-white">✨</span>
    </div>

    <!-- 消息气泡 -->
    <div
      class="max-w-[75%] rounded-2xl px-4 py-2.5 text-[14px] leading-relaxed transition-shadow duration-200"
      :class="isUser
        ? 'bg-gray-900 text-white'
        : 'bg-white text-gray-800 ring-1 ring-gray-200/80 shadow-sm hover:shadow'"
    >
      <pre class="whitespace-pre-wrap break-words font-sans">{{ msg.content }}</pre>
    </div>

    <!-- 用户头像 -->
    <div
      v-if="isUser"
      class="flex h-7 w-7 flex-shrink-0 items-center justify-center rounded-lg bg-gradient-to-br from-indigo-500 to-violet-500 text-[12px] font-semibold text-white shadow-sm"
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
