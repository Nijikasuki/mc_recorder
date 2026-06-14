<template>
  <aside class="flex h-full w-64 flex-shrink-0 flex-col border-r border-gray-200/80 bg-gray-50/50">
    <!-- 新对话按钮 -->
    <div class="p-3">
      <button
        @click="$emit('new-chat')"
        class="group flex w-full items-center justify-center gap-2 rounded-lg bg-gray-900 px-3 py-2 text-sm font-medium text-white transition-all duration-200 hover:bg-gray-800 active:scale-[0.98]"
      >
        <svg class="h-4 w-4 transition-transform duration-200 group-hover:rotate-90" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4" />
        </svg>
        <span>新对话</span>
      </button>
    </div>

    <!-- 会话列表 -->
    <div class="flex-1 overflow-y-auto px-2 pb-4">
      <div v-if="sessions.length === 0" class="mt-6 text-center text-xs text-gray-400">
        点上面"新对话"开始
      </div>

      <TransitionGroup
        v-else
        tag="div"
        class="space-y-0.5"
        enter-active-class="transition-all duration-200 ease-out"
        enter-from-class="opacity-0 -translate-x-1"
        enter-to-class="opacity-100 translate-x-0"
        leave-active-class="transition-all duration-150 ease-in"
        leave-from-class="opacity-100"
        leave-to-class="opacity-0"
      >
        <ChatSessionItem
          v-for="session in sessions"
          :key="session.id"
          :session="session"
          :is-active="currentSessionId === session.id"
          @switch="$emit('switch-session', session.id)"
          @delete="$emit('delete-session', session.id)"
        />
      </TransitionGroup>
    </div>
  </aside>
</template>

<script setup>
import ChatSessionItem from './ChatSessionItem.vue'

defineProps({
  sessions: { type: Array, required: true },
  currentSessionId: { type: String, default: null },
  username: { type: String, default: '' },
})

defineEmits(['new-chat', 'switch-session', 'delete-session'])
</script>
