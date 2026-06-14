<template>
  <div ref="listRef" class="flex-1 overflow-y-auto scroll-smooth">
    <div class="mx-auto max-w-5xl px-8 py-6">
      <!-- 空状态: 示例 prompts -->
      <div v-if="messages.length === 0 && !loading" class="mt-10 text-center">
        <div class="mb-4 text-5xl">👋</div>
        <h1 class="text-2xl font-semibold tracking-tight text-gray-900">
          你好, 我是 Tethys
        </h1>
        <p class="mt-2 text-sm text-gray-500">试试问我以下问题</p>

        <div class="mt-8 grid grid-cols-1 gap-2.5 sm:grid-cols-2">
          <button
            v-for="prompt in samplePrompts"
            :key="prompt"
            @click="$emit('send-prompt', prompt)"
            class="group rounded-xl border border-gray-200/80 bg-white p-3.5 text-left text-[13px] text-gray-700 transition-all duration-200 hover:border-gray-300 hover:bg-gray-50 hover:shadow-sm active:scale-[0.99]"
          >
            {{ prompt }}
          </button>
        </div>
      </div>

      <!-- 消息列表 -->
      <TransitionGroup
        v-else
        tag="div"
        class="space-y-4"
        enter-active-class="transition-all duration-300 ease-out"
        enter-from-class="opacity-0 translate-y-2"
        enter-to-class="opacity-100 translate-y-0"
      >
        <MessageBubble
          v-for="(msg, idx) in messages"
          :key="idx"
          :msg="msg"
          :username="username"
        />
        <ThinkingIndicator v-if="loading" key="thinking" :node-name="thinkingNode" />
      </TransitionGroup>
    </div>
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

async function scrollToBottom() {
  await nextTick()
  if (listRef.value) {
    listRef.value.scrollTo({ top: listRef.value.scrollHeight, behavior: 'smooth' })
  }
}

watch(() => props.messages.length, scrollToBottom)
watch(() => props.loading, scrollToBottom)
watch(() => props.messages.map(m => m.content).join(''), scrollToBottom)
</script>
