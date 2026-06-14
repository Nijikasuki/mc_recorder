<template>
  <div
    @click="$emit('switch')"
    class="group relative cursor-pointer rounded-lg px-2.5 py-2 transition-all duration-150"
    :class="isActive
      ? 'bg-white shadow-sm ring-1 ring-gray-200/80'
      : 'hover:bg-white/70'"
  >
    <div class="truncate pr-6 text-[13px] font-medium" :class="isActive ? 'text-gray-900' : 'text-gray-700'">
      {{ session.title }}
    </div>
    <div class="mt-0.5 text-[11px] text-gray-400">
      {{ formatDate(session.createdAt) }}
    </div>

    <!-- 删除按钮 -->
    <button
      @click.stop="$emit('delete')"
      class="absolute right-1.5 top-2 flex h-5 w-5 items-center justify-center rounded text-gray-400 opacity-0 transition-all duration-150 hover:bg-red-50 hover:text-red-500 group-hover:opacity-100 active:scale-90"
      title="删除会话"
    >
      <svg class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
        <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
      </svg>
    </button>
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
