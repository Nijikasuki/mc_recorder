<template>
  <div class="fixed inset-0 overflow-hidden bg-gradient-to-br from-gray-950 via-blue-950 to-purple-950">
    <!-- 装饰: 漂浮的光晕 -->
    <div class="pointer-events-none absolute inset-0 overflow-hidden">
      <div class="absolute -left-32 top-20 h-96 w-96 rounded-full bg-blue-500/10 blur-3xl"></div>
      <div class="absolute -right-32 bottom-20 h-96 w-96 rounded-full bg-purple-500/10 blur-3xl"></div>
      <div class="absolute left-1/2 top-1/2 h-80 w-80 -translate-x-1/2 -translate-y-1/2 rounded-full bg-indigo-500/5 blur-3xl"></div>
    </div>

    <!-- 主体: Sidebar + Main 两栏 -->
    <div class="relative flex h-full">
      <slot name="sidebar" />
      <slot name="main" />
    </div>
  </div>
</template>

<script setup>
</script>
