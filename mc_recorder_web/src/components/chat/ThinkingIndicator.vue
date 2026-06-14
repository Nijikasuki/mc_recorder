<template>
  <div class="flex gap-3">
    <div class="flex h-7 w-7 flex-shrink-0 items-center justify-center rounded-lg bg-gradient-to-br from-indigo-500 to-violet-500 text-sm shadow-sm">
      <span class="text-white">✨</span>
    </div>

    <div class="rounded-2xl bg-white px-4 py-2.5 shadow-sm ring-1 ring-gray-200/80">
      <div class="flex items-center gap-2.5">
        <div class="flex gap-1">
          <span class="h-1.5 w-1.5 animate-bounce rounded-full bg-indigo-400" style="animation-delay: 0ms"></span>
          <span class="h-1.5 w-1.5 animate-bounce rounded-full bg-violet-400" style="animation-delay: 150ms"></span>
          <span class="h-1.5 w-1.5 animate-bounce rounded-full bg-purple-400" style="animation-delay: 300ms"></span>
        </div>
        <span class="text-[13px] text-gray-600">{{ thinkingText }}</span>
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
