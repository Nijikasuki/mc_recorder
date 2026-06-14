<template>
  <div class="flex flex-wrap gap-2">
    <button
      @click="$emit('update:enableSearch', !enableSearch)"
      class="group flex items-center gap-1.5 rounded-full border px-3 py-1 text-[12px] font-medium transition-all duration-200 active:scale-[0.97]"
      :class="enableSearch
        ? 'border-blue-400/30 bg-blue-400/10 text-blue-300'
        : 'border-[#1F242C] bg-[#161B22] text-[#8B949E] hover:border-[#2A3038] hover:text-[#E6E6E8]'"
      title="联网搜索 (Tavily)"
    >
      <svg class="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.8">
        <circle cx="12" cy="12" r="9" />
        <path d="M3 12h18M12 3a13 13 0 010 18M12 3a13 13 0 000 18" />
      </svg>
      <span>联网搜索</span>
    </button>

    <button
      @click="$emit('update:enableKnowledge', !enableKnowledge)"
      class="group flex items-center gap-1.5 rounded-full border px-3 py-1 text-[12px] font-medium transition-all duration-200 active:scale-[0.97]"
      :class="enableKnowledge
        ? 'border-violet-400/30 bg-violet-400/10 text-violet-300'
        : 'border-[#1F242C] bg-[#161B22] text-[#8B949E] hover:border-[#2A3038] hover:text-[#E6E6E8]'"
      title="鸣潮百科 (RAG)"
    >
      <svg class="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.8">
        <path d="M4 4h12a4 4 0 014 4v12H8a4 4 0 01-4-4V4z" />
        <path d="M4 4v16M8 8h8M8 12h8" />
      </svg>
      <span>知识库</span>
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
