<template>
  <header class="flex items-center justify-between border-b border-white/5 bg-white/[0.02] px-6 py-4 backdrop-blur-xl">
    <div class="flex items-center gap-3">
      <!-- AI 头像 -->
      <div class="relative">
        <div class="flex h-10 w-10 items-center justify-center rounded-2xl bg-gradient-to-br from-blue-500 to-purple-500 text-xl shadow-lg shadow-purple-500/30">
          🤖
        </div>
        <div class="absolute -bottom-0.5 -right-0.5 flex h-3 w-3 items-center justify-center">
          <span class="absolute h-full w-full animate-ping rounded-full bg-green-400/60"></span>
          <span class="relative h-2 w-2 rounded-full bg-green-400"></span>
        </div>
      </div>

      <!-- 标题 -->
      <div>
        <h2 class="bg-gradient-to-r from-white to-gray-300 bg-clip-text text-base font-bold text-transparent">
          Tethys
        </h2>
        <p class="text-xs text-gray-500">
          鸣潮智能助手 · GLM-4-plus
        </p>
      </div>
    </div>

    <!-- 右侧装饰标签 -->
    <div class="hidden items-center gap-2 sm:flex">
      <span class="rounded-full border border-blue-400/20 bg-blue-400/10 px-3 py-1 text-xs text-blue-300">
        LangGraph
      </span>
      <span class="rounded-full border border-purple-400/20 bg-purple-400/10 px-3 py-1 text-xs text-purple-300">
        RAG
      </span>
    </div>
  </header>
</template>

<script setup>
</script>
