<template>
  <header class="flex items-center justify-between border-b border-[#1F242C] bg-[#0D1117]/95 px-6 py-3 backdrop-blur-xl">
    <div class="flex items-center gap-3">
      <!-- AI 头像 -->
      <div class="relative">
        <div class="flex h-9 w-9 items-center justify-center rounded-xl bg-gradient-to-br from-cyan-400 to-blue-500 shadow-lg shadow-blue-500/20">
          <svg class="h-4 w-4 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9.5 3l1.5 4 4 1.5-4 1.5-1.5 4-1.5-4L4 8.5l4-1.5L9.5 3zm7 8l1 2.5 2.5 1-2.5 1-1 2.5-1-2.5L13 14.5l2.5-1 1-2.5z" />
          </svg>
        </div>
        <span class="absolute -bottom-0.5 -right-0.5 h-2.5 w-2.5 rounded-full border-2 border-[#0D1117] bg-emerald-400"></span>
      </div>

      <div>
        <h2 class="text-[15px] font-semibold text-[#E6E6E8]">Tethys</h2>
        <p class="text-[11px] text-[#8B949E]">鸣潮智能助手 · GLM-4-plus</p>
      </div>
    </div>

    <!-- 右侧标签 -->
    <div class="hidden items-center gap-2 sm:flex">
      <span class="rounded-md bg-violet-500/10 px-2 py-0.5 text-[11px] font-medium text-violet-300">
        LangGraph
      </span>
      <span class="rounded-md bg-blue-500/10 px-2 py-0.5 text-[11px] font-medium text-blue-300">
        RAG
      </span>
    </div>
  </header>
</template>

<script setup>
</script>
