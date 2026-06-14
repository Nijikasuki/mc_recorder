<template>
  <div class="flex flex-wrap gap-1.5">
    <button
      @click="$emit('update:enableSearch', !enableSearch)"
      class="group flex items-center gap-1.5 rounded-lg border px-2.5 py-1 text-[12px] font-medium transition-all duration-200 active:scale-[0.97]"
      :class="enableSearch
        ? 'border-indigo-200 bg-indigo-50 text-indigo-700 shadow-sm'
        : 'border-gray-200 bg-white text-gray-600 hover:border-gray-300 hover:bg-gray-50'"
      title="开启后 AI 可以联网搜索实时信息 (Tavily)"
    >
      <span>🌐</span>
      <span>联网搜索</span>
    </button>

    <button
      @click="$emit('update:enableKnowledge', !enableKnowledge)"
      class="group flex items-center gap-1.5 rounded-lg border px-2.5 py-1 text-[12px] font-medium transition-all duration-200 active:scale-[0.97]"
      :class="enableKnowledge
        ? 'border-violet-200 bg-violet-50 text-violet-700 shadow-sm'
        : 'border-gray-200 bg-white text-gray-600 hover:border-gray-300 hover:bg-gray-50'"
      title="开启后 AI 从鸣潮百科 (RAG) 查资料"
    >
      <span>📚</span>
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
