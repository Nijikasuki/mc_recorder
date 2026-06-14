<template>
  <div
    ref="listRef"
    class="flex-1 overflow-y-auto scroll-smooth px-6 py-6"
  >
    <!-- 空状态: 示例 prompts -->
    <div v-if="messages.length === 0 && !loading" class="mx-auto mt-12 max-w-2xl text-center">
      <div class="mb-3 text-6xl">👋</div>
      <h1 class="bg-gradient-to-r from-blue-300 via-purple-300 to-pink-300 bg-clip-text text-2xl font-bold text-transparent">
        你好, 我是 Tethys
      </h1>
      <p class="mt-2 text-sm text-gray-400">试试问我以下问题</p>

      <div class="mt-8 grid grid-cols-1 gap-3 sm:grid-cols-2">
        <button
          v-for="(prompt, idx) in samplePrompts"
          :key="prompt"
          @click="$emit('send-prompt', prompt)"
          class="group rounded-2xl border border-white/10 bg-white/[0.03] p-4 text-left text-sm text-gray-300 backdrop-blur-xl transition-all duration-300 ease-out hover:scale-[1.02] hover:border-blue-400/30 hover:bg-white/[0.06] hover:shadow-xl hover:shadow-blue-500/10 active:scale-[0.98]"
          :style="{ animationDelay: `${idx * 80}ms` }"
        >
          <span class="block text-gray-200 transition-colors group-hover:text-white">
            {{ prompt }}
          </span>
        </button>
      </div>
    </div>

    <!-- 消息列表 -->
    <TransitionGroup
      v-else
      tag="div"
      class="space-y-4"
      enter-active-class="transition-all duration-400 ease-out"
      enter-from-class="opacity-0 translate-y-3"
      enter-to-class="opacity-100 translate-y-0"
    >
      <MessageBubble
        v-for="(msg, idx) in messages"
        :key="idx"
        :msg="msg"
        :username="username"
      />

      <!-- 思考中提示 -->
      <ThinkingIndicator v-if="loading" key="thinking" :node-name="thinkingNode" />
    </TransitionGroup>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import MessageBubble from './MessageBubble.vue'
import ThinkingIndicator from './ThinkingIndicator.vue'

const props = defineProps({
  messages: { type: Array, required: true },
  loading: { type: Boolean, default: false },
  thinkingNode: { type: String, default: '' },
  username: { type: String, default: '' },
})

defineEmits(['send-prompt'])

const samplePrompts = [
  '琳奈是什么属性? 介绍下她',
  '我有哪些角色, 推荐重点培养谁?',
  '热熔属性的5星角色都有谁?',
  '今天有什么科技新闻',
]

const listRef = ref(null)

// 自动滚动到底
async function scrollToBottom() {
  await nextTick()
  if (listRef.value) {
    listRef.value.scrollTo({
      top: listRef.value.scrollHeight,
      behavior: 'smooth',
    })
  }
}

watch(() => props.messages.length, scrollToBottom)
watch(() => props.loading, scrollToBottom)
watch(() => props.messages.map(m => m.content).join(''), scrollToBottom)
</script>
