<template>
  <aside class="flex h-full w-72 flex-shrink-0 flex-col border-r border-white/5 bg-white/[0.02] backdrop-blur-xl">
    <!-- 新对话按钮 -->
    <div class="p-4">
      <button
        @click="$emit('new-chat')"
        class="group flex w-full items-center justify-center gap-2 rounded-2xl bg-gradient-to-r from-blue-500/90 to-purple-500/90 px-4 py-3 text-sm font-medium text-white shadow-lg shadow-blue-500/20 transition-all duration-300 ease-out hover:scale-[1.02] hover:shadow-xl hover:shadow-purple-500/30 active:scale-[0.97]"
      >
        <svg class="h-4 w-4 transition-transform duration-300 group-hover:rotate-90" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4" />
        </svg>
        <span>新对话</span>
      </button>
    </div>

    <!-- 会话列表 -->
    <div class="flex-1 overflow-y-auto px-3 pb-4">
      <div v-if="sessions.length === 0" class="mt-8 text-center text-xs text-gray-500">
        点上面"新对话"开始
      </div>

      <TransitionGroup
        v-else
        tag="div"
        class="space-y-1.5"
        enter-active-class="transition-all duration-300 ease-out"
        enter-from-class="opacity-0 -translate-x-2"
        enter-to-class="opacity-100 translate-x-0"
        leave-active-class="transition-all duration-200 ease-in"
        leave-from-class="opacity-100"
        leave-to-class="opacity-0 -translate-x-2"
      >
        <ChatSessionItem
          v-for="(session, idx) in sessions"
          :key="session.id"
          :session="session"
          :is-active="currentSessionId === session.id"
          :style="{ transitionDelay: `${idx * 30}ms` }"
          @switch="$emit('switch-session', session.id)"
          @delete="$emit('delete-session', session.id)"
        />
      </TransitionGroup>
    </div>

    <!-- 底部用户信息 -->
    <div v-if="username" class="border-t border-white/5 p-4">
      <div class="flex items-center gap-3 rounded-xl bg-white/[0.03] p-2.5">
        <div class="flex h-8 w-8 items-center justify-center rounded-full bg-gradient-to-br from-pink-400 to-orange-400 text-sm font-bold text-white">
          {{ username.charAt(0).toUpperCase() }}
        </div>
        <div class="text-sm text-gray-300">{{ username }}</div>
      </div>
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
