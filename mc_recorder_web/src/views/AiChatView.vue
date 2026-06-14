<template>
  <ChatLayout>
    <!-- 左侧栏 -->
    <template #sidebar>
      <ChatSidebar
        :sessions="store.sessions"
        :current-session-id="store.currentSessionId"
        :username="userStore.username"
        @new-chat="handleNewChat"
        @switch-session="store.switchSession"
        @delete-session="handleDelete"
      />
    </template>

    <!-- 主区域 -->
    <template #main>
      <main class="flex flex-1 flex-col overflow-hidden">
        <!-- 顶部 -->
        <ChatHeader />

        <!-- 消息列表 -->
        <MessageList
          :messages="messages"
          :loading="loading"
          :thinking-node="thinkingNode"
          :username="userStore.username"
          @send-prompt="handlePromptClick"
        />

        <!-- 底部: 工具开关 + 输入框 -->
        <div class="border-t border-white/5 bg-white/[0.02] px-6 py-4 backdrop-blur-xl">
          <div class="mx-auto max-w-4xl space-y-3">
            <ToolToggleBar
              v-model:enableSearch="enableSearch"
              v-model:enableKnowledge="enableKnowledge"
            />
            <InputBar
              v-model="inputText"
              :loading="loading"
              @send="handleSend"
            />
          </div>
        </div>
      </main>
    </template>
  </ChatLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { streamChat } from '../api/ai'
import { useUserStore } from '../store/user'
import { useConversationStore } from '../store/conversation'

import ChatLayout from '../components/chat/ChatLayout.vue'
import ChatSidebar from '../components/chat/ChatSidebar.vue'
import ChatHeader from '../components/chat/ChatHeader.vue'
import MessageList from '../components/chat/MessageList.vue'
import ToolToggleBar from '../components/chat/ToolToggleBar.vue'
import InputBar from '../components/chat/InputBar.vue'

const userStore = useUserStore()
const store = useConversationStore()

const inputText = ref('')
const loading = ref(false)
const abortController = ref(null)

const enableSearch = ref(false)
const enableKnowledge = ref(false)

const thinkingNode = ref('')

const messages = computed(() => store.currentMessages)

onMounted(() => {
  store.ensureCurrentSession()
})

function handleNewChat() {
  if (loading.value) {
    abortController.value?.abort()
    loading.value = false
  }
  store.createSession()
}

async function handleDelete(id) {
  const session = store.sessions.find(s => s.id === id)
  try {
    await ElMessageBox.confirm(
      `确定删除 "${session?.title || '此会话'}" ?`,
      '删除会话',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning',
        customClass: 'dark-confirm',
      }
    )
    store.deleteSession(id)
    ElMessage.success('已删除')
    if (store.sessions.length === 0) {
      store.createSession()
    }
  } catch {
    // 取消
  }
}

function handlePromptClick(prompt) {
  inputText.value = prompt
  handleSend()
}

async function handleSend() {
  const text = inputText.value.trim()
  if (!text || loading.value) return

  store.ensureCurrentSession()
  store.addMessage('user', text)
  inputText.value = ''

  loading.value = true
  thinkingNode.value = ''
  abortController.value = new AbortController()

  let aiContent = ''
  let firstTokenReceived = false

  try {
    await streamChat(
      {
        message: text,
        conversation_id: store.currentSessionId,
        enable_search: enableSearch.value,
        enable_knowledge: enableKnowledge.value,
      },
      {
        onToken: (token) => {
          if (!firstTokenReceived) {
            firstTokenReceived = true
            loading.value = false
            store.addMessage('assistant', '')
          }
          aiContent += token
          store.updateLastMessage(aiContent)
        },
        onNode: (node) => {
          thinkingNode.value = node
        },
        onDone: () => {
          loading.value = false
          thinkingNode.value = ''
        },
      },
      abortController.value.signal,
    )
  } catch (err) {
    if (err.name === 'AbortError') return
    ElMessage.error('AI 出错了: ' + (err.message || ''))
    loading.value = false
    thinkingNode.value = ''
  }
}
</script>

<style>
/* 全局滚动条美化 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.12);
  border-radius: 3px;
  transition: background 0.3s;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.25);
}

/* 平滑滚动 */
html {
  scroll-behavior: smooth;
}

/* 文本选中色 */
::selection {
  background: rgba(139, 92, 246, 0.35);
  color: #fff;
}

/* Element Plus MessageBox 暗色化 */
.dark-confirm {
  background: rgba(20, 20, 30, 0.95) !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  backdrop-filter: blur(16px);
}
</style>
