<template>
  <div
    @click="$emit('switch')"
    class="group relative cursor-pointer rounded-lg px-2.5 py-2 transition-all duration-150"
    :class="isActive
      ? 'bg-[#1F242C]'
      : 'hover:bg-[#161B22]'"
  >
    <div class="flex items-center justify-between gap-2">
      <div class="min-w-0 flex-1">
        <div class="truncate text-[13px]" :class="isActive ? 'text-[#E6E6E8] font-medium' : 'text-[#8B949E] group-hover:text-[#E6E6E8]'">
          {{ session.title }}
        </div>
        <div class="mt-0.5 text-[11px]" :class="isActive ? 'text-amber-400/70' : 'text-[#484F58]'">
          {{ formatDate(session.createdAt) }}
        </div>
      </div>

      <button
        @click.stop="$emit('delete')"
        class="flex h-5 w-5 flex-shrink-0 items-center justify-center rounded text-[#484F58] opacity-0 transition-all duration-150 hover:bg-rose-500/10 hover:text-rose-400 group-hover:opacity-100 active:scale-90"
        title="删除会话"
      >
        <svg class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>
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
