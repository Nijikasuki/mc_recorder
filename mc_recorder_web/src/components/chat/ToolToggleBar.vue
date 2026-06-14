<template>
  <div class="flex flex-wrap gap-2">
    <button
      @click="$emit('update:enableSearch', !enableSearch)"
      class="group flex items-center gap-2 rounded-full border px-4 py-1.5 text-xs font-medium transition-all duration-300 ease-out active:scale-[0.95]"
      :class="enableSearch
        ? 'border-blue-400/40 bg-gradient-to-r from-blue-500/20 to-purple-500/20 text-blue-200 shadow-lg shadow-blue-500/20'
        : 'border-white/10 bg-white/[0.03] text-gray-400 hover:border-white/20 hover:text-gray-200'"
      title="开启后 AI 可以联网搜索实时信息 (Tavily)"
    >
      <span class="text-sm transition-transform duration-300 group-hover:scale-110">🌐</span>
      <span>联网搜索</span>
      <span v-if="enableSearch" class="ml-0.5 h-1.5 w-1.5 animate-pulse rounded-full bg-blue-400"></span>
    </button>

    <button
      @click="$emit('update:enableKnowledge', !enableKnowledge)"
      class="group flex items-center gap-2 rounded-full border px-4 py-1.5 text-xs font-medium transition-all duration-300 ease-out active:scale-[0.95]"
      :class="enableKnowledge
        ? 'border-cyan-400/40 bg-gradient-to-r from-cyan-500/20 to-blue-500/20 text-cyan-200 shadow-lg shadow-cyan-500/20'
        : 'border-white/10 bg-white/[0.03] text-gray-400 hover:border-white/20 hover:text-gray-200'"
      title="开启后 AI 从鸣潮百科 (RAG) 查资料"
    >
      <span class="text-sm transition-transform duration-300 group-hover:scale-110">📚</span>
      <span>知识库</span>
      <span v-if="enableKnowledge" class="ml-0.5 h-1.5 w-1.5 animate-pulse rounded-full bg-cyan-400"></span>
    </button>
  </div>
</template>

<script setup>
defineProps({
  enableSearch: { type: Boolean, default: false },
  enableKnowledge: { type: Boolean, default: false },
})

defineEmits(['update:enableSearch', 'update:enableKnowledge'])
</script>
