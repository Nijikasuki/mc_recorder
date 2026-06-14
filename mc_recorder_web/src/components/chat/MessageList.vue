<template>
  <div ref="listRef" class="flex-1 overflow-y-auto scroll-smooth bg-[#0D1117]">
    <div class="mx-auto max-w-5xl px-8 py-6">
      <!-- 空状态 -->
      <div v-if="messages.length === 0 && !loading" class="mt-8">
        <div class="text-center">
          <div class="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-400 to-blue-500 shadow-lg shadow-blue-500/20">
            <svg class="h-7 w-7 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9.5 3l1.5 4 4 1.5-4 1.5-1.5 4-1.5-4L4 8.5l4-1.5L9.5 3zm7 8l1 2.5 2.5 1-2.5 1-1 2.5-1-2.5L13 14.5l2.5-1 1-2.5z" />
            </svg>
          </div>
          <h1 class="text-[22px] font-semibold text-[#E6E6E8]">你好, 我是 Tethys</h1>
          <p class="mt-1 text-[13px] text-[#8B949E]">鸣潮智能助手, 来问点什么吧</p>
        </div>

        <div class="mt-8 grid grid-cols-1 gap-3 sm:grid-cols-2">
          <button
            v-for="(prompt, idx) in samplePrompts"
            :key="prompt"
            @click="$emit('send-prompt', prompt)"
            class="group flex items-start gap-3 rounded-xl border border-[#1F242C] bg-[#161B22] p-3.5 text-left transition-all duration-200 hover:border-[#2A3038] hover:bg-[#1F242C] active:scale-[0.99]"
          >
            <div
              class="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-lg"
              :class="promptColors[idx]"
            >
              <component :is="promptIcons[idx]" class="h-4 w-4" />
            </div>
            <span class="flex-1 text-[13px] leading-relaxed text-[#E6E6E8]">
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
import { ref, watch, nextTick, h } from 'vue'
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

// 彩色图标方块 — 像酷狗游戏首页那种
const promptColors = [
  'bg-amber-500/15 text-amber-400',
  'bg-violet-500/15 text-violet-400',
  'bg-rose-500/15 text-rose-400',
  'bg-blue-500/15 text-blue-400',
]

const Sparkle = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M9.5 3l1.5 4 4 1.5-4 1.5-1.5 4-1.5-4L4 8.5l4-1.5L9.5 3z' }),
])
const Shield = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M12 2L20 6v7c0 5-4 8-8 9-4-1-8-4-8-9V6l8-4z' }),
])
const Flame = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M12 2c2 4 5 6 5 11a5 5 0 01-10 0c0-2 1-4 2-5 1 1 2 2 2 3 1-3 0-6 1-9z' }),
])
const Globe = (_, { attrs }) => h('svg', { ...attrs, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', 'stroke-width': '1.8' }, [
  h('circle', { cx: '12', cy: '12', r: '9' }),
  h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M3 12h18M12 3a13 13 0 010 18M12 3a13 13 0 000 18' }),
])
const promptIcons = [Sparkle, Shield, Flame, Globe]

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
