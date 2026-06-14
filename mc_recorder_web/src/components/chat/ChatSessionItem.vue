<template>
  <div
    @click="$emit('switch')"
    class="group relative cursor-pointer rounded-xl border border-transparent px-3 py-2.5 transition-all duration-200 ease-out"
    :class="isActive
      ? 'bg-white/[0.08] border-white/10 shadow-lg shadow-blue-500/5'
      : 'hover:bg-white/[0.04] hover:border-white/5'"
  >
    <!-- 标题 -->
    <div class="truncate pr-7 text-sm font-medium" :class="isActive ? 'text-white' : 'text-gray-300'">
      {{ session.title }}
    </div>

    <!-- 时间 -->
    <div class="mt-0.5 text-xs text-gray-500">
      {{ formatDate(session.createdAt) }}
    </div>

    <!-- 删除按钮 (hover 显示) -->
    <button
      @click.stop="$emit('delete')"
      class="absolute right-2 top-2.5 flex h-6 w-6 items-center justify-center rounded-md text-gray-500 opacity-0 transition-all duration-200 hover:bg-red-500/20 hover:text-red-400 group-hover:opacity-100 active:scale-90"
      title="删除会话"
    >
      <svg class="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6M1 7h22M9 7V4a2 2 0 012-2h2a2 2 0 012 2v3" />
      </svg>
    </button>

    <!-- 左侧高亮条 -->
    <div
      v-if="isActive"
      class="absolute -left-3 top-1/2 h-6 w-1 -translate-y-1/2 rounded-full bg-gradient-to-b from-blue-400 to-purple-400"
    ></div>
  </div>
</template>

<script setup>
defineProps({
  session: { type: Object, required: true },
  isActive: { type: Boolean, default: false },
})

defineEmits(['switch', 'delete'])

function formatDate(ts) {
  if (!ts) return ''
  const d = new Date(ts)
  const now = new Date()
  const diffMin = Math.floor((now - d) / 60000)
  if (diffMin < 1) return '刚刚'
  if (diffMin < 60) return `${diffMin} 分钟前`
  if (diffMin < 1440) return `${Math.floor(diffMin / 60)} 小时前`
  return `${d.getMonth() + 1}/${d.getDate()}`
}
</script>
