import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

const STORAGE_KEY = 'tethys_conversations'

// 加载本地存储数据
function loadFromStorage() {
  try {
    const data = JSON.parse(localStorage.getItem(STORAGE_KEY) || '{}')
    return {
      sessions: data.sessions || [],
      currentSessionId: data.currentSessionId || null,
    }
  } catch (e) {
    return { sessions: [], currentSessionId: null }
  }
}

export const useConversationStore = defineStore('conversation', () => {
  const initial = loadFromStorage()
  const sessions = ref(initial.sessions)
  const currentSessionId = ref(initial.currentSessionId)

  // 当前会话对象
  const currentSession = computed(() =>
    sessions.value.find(s => s.id === currentSessionId.value) || null
  )

  // 当前会话的消息列表
  const currentMessages = computed(() =>
    currentSession.value?.messages || []
  )

  // 写入 localStorage (每次修改后调用)
  function save() {
    localStorage.setItem(STORAGE_KEY, JSON.stringify({
      sessions: sessions.value,
      currentSessionId: currentSessionId.value,
    }))
  }

  // 简易 UUID
  function genId() {
    return 'sess-' + Date.now() + '-' + Math.random().toString(36).slice(2, 9)
  }

  // 新建会话, 返回新会话 id
  function createSession() {
    const newSession = {
      id: genId(),
      title: '新对话',
      messages: [],
      createdAt: Date.now(),
      updatedAt: Date.now(),
    }
    sessions.value.unshift(newSession)
    currentSessionId.value = newSession.id
    save()
    return newSession.id
  }

  // 切换会话
  function switchSession(id) {
    currentSessionId.value = id
    save()
  }

  // 删除会话
  function deleteSession(id) {
    const idx = sessions.value.findIndex(s => s.id === id)
    if (idx === -1) return
    sessions.value.splice(idx, 1)
    if (currentSessionId.value === id) {
      currentSessionId.value = sessions.value[0]?.id || null
    }
    save()
  }

  // 给当前会话加消息. 没有当前会话则自动建一个
  function addMessage(role, content) {
    if (!currentSession.value) {
      createSession()
    }
    currentSession.value.messages.push({ role, content })
    currentSession.value.updatedAt = Date.now()
    // 首条用户消息自动作为标题(前 20 字)
    if (currentSession.value.title === '新对话' && role === 'user') {
      currentSession.value.title = content.slice(0, 20) || '新对话'
    }
    save()
  }

  // 更新最后一条 assistant 消息的内容(打字效果用)
  function updateLastMessage(content) {
    if (!currentSession.value) return
    const msgs = currentSession.value.messages
    const last = msgs[msgs.length - 1]
    if (last && last.role === 'assistant') {
      last.content = content
      save()
    }
  }

  // 确保有一个当前会话(打开 AI 页面时调用)
  function ensureCurrentSession() {
    if (!currentSession.value) {
      createSession()
    }
  }

  return {
    sessions,
    currentSessionId,
    currentSession,
    currentMessages,
    createSession,
    switchSession,
    deleteSession,
    addMessage,
    updateLastMessage,
    ensureCurrentSession,
  }
})
