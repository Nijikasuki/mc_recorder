<template>
  <header class="flex items-center justify-between border-b border-gray-200/80 bg-white/80 px-6 py-3 backdrop-blur-xl">
    <div class="flex items-center gap-2.5">
      <div class="relative">
        <div class="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-indigo-500 to-violet-500 text-base shadow-sm">
          <span class="text-white">✨</span>
        </div>
        <span class="absolute -bottom-0.5 -right-0.5 h-2.5 w-2.5 rounded-full border-2 border-white bg-emerald-400"></span>
      </div>
      <div>
        <h2 class="text-[14px] font-semibold tracking-tight text-gray-900">Tethys</h2>
        <p class="text-[11px] text-gray-500">鸣潮智能助手 · GLM-4-plus</p>
      </div>
    </div>

    <!-- 标签 -->
    <div class="hidden items-center gap-1.5 sm:flex">
      <span class="rounded-md bg-indigo-50 px-2 py-0.5 text-[11px] font-medium text-indigo-600">
        LangGraph
      </span>
      <span class="rounded-md bg-violet-50 px-2 py-0.5 text-[11px] font-medium text-violet-600">
        RAG
      </span>
    </div>
  </header>
</template>

<script setup>
</script>
