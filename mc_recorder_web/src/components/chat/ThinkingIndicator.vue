<template>
  <div class="flex gap-3">
    <div class="flex h-7 w-7 flex-shrink-0 items-center justify-center rounded-lg bg-gradient-to-br from-cyan-400 to-blue-500 shadow-md shadow-blue-500/20">
      <svg class="h-3.5 w-3.5 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M9.5 3l1.5 4 4 1.5-4 1.5-1.5 4-1.5-4L4 8.5l4-1.5L9.5 3z" />
      </svg>
    </div>

    <div class="rounded-2xl border border-[#1F242C] bg-[#161B22] px-4 py-2.5">
      <div class="flex items-center gap-2.5">
        <div class="flex gap-1">
          <span class="h-1.5 w-1.5 animate-bounce rounded-full bg-amber-400" style="animation-delay: 0ms"></span>
          <span class="h-1.5 w-1.5 animate-bounce rounded-full bg-violet-400" style="animation-delay: 150ms"></span>
          <span class="h-1.5 w-1.5 animate-bounce rounded-full bg-blue-400" style="animation-delay: 300ms"></span>
        </div>
        <span class="text-[12px] text-[#8B949E]">{{ thinkingText }}</span>
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
  retrieve_rag: '检索鸣潮百科',
  llm: '思考中',
  tools: '调用工具',
}

const thinkingText = computed(() => TEXT_MAP[props.nodeName] || '思考中')
</script>
