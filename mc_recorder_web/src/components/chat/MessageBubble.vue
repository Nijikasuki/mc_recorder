<template>
  <div
    class="flex gap-3"
    :class="isUser ? 'justify-end' : 'justify-start'"
  >
    <!-- AI 头像 -->
    <div v-if="!isUser" class="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-blue-500 to-purple-500 text-base shadow-lg shadow-purple-500/20">
      🤖
    </div>

    <!-- 消息气泡 -->
    <div
      class="max-w-2xl rounded-2xl px-4 py-3 shadow-lg transition-all duration-300 hover:shadow-xl"
      :class="isUser
        ? 'bg-gradient-to-br from-blue-500/90 to-purple-500/90 text-white shadow-purple-500/20'
        : 'border border-white/10 bg-white/[0.05] text-gray-100 backdrop-blur-xl shadow-blue-500/5'"
    >
      <pre class="whitespace-pre-wrap break-words font-sans text-sm leading-relaxed">{{ msg.content }}</pre>
    </div>

    <!-- 用户头像 -->
    <div v-if="isUser" class="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-pink-400 to-orange-400 text-sm font-bold text-white shadow-lg shadow-pink-500/20">
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
