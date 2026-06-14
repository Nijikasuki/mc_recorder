<template>
  <div class="flex bg-white rounded-2xl shadow-sm overflow-hidden"
       style="height: calc(100vh - 112px);">

    <!-- ========== 左侧栏 ========== -->
    <div class="flex flex-col border-r bg-gray-50" style="width: 260px; flex-shrink: 0;">
      <!-- 新对话按钮 -->
      <div class="p-3 border-b">
        <button @click="handleNewChat"
                class="w-full py-2 px-3 rounded-lg text-white font-medium text-sm hover:opacity-90 transition flex items-center justify-center gap-2"
                style="background: linear-gradient(135deg, #667eea, #764ba2);">
          <span class="text-base">🆕</span>
          <span>新对话</span>
        </button>
      </div>

      <!-- 会话列表 -->
      <div class="flex-1 overflow-y-auto">
        <div v-if="store.sessions.length === 0" class="p-6 text-center text-xs text-gray-400">
          点上面"新对话"开始
        </div>

        <div v-for="session in store.sessions" :key="session.id"
             @click="store.switchSession(session.id)"
             class="group relative px-3 py-2.5 cursor-pointer border-l-4 transition"
             :class="store.currentSessionId === session.id
                ? 'bg-white border-l-purple-500 shadow-sm'
                : 'border-l-transparent hover:bg-gray-100'">
          <div class="text-sm text-gray-800 truncate pr-6 font-medium">{{ session.title }}</div>
          <div class="text-xs text-gray-400 mt-0.5">{{ formatDate(session.createdAt) }}</div>

          <!-- 删除按钮 (悬停显示) -->
          <button @click.stop="handleDelete(session.id)"
                  class="absolute right-2 top-2.5 w-6 h-6 rounded text-gray-400 opacity-0 group-hover:opacity-100 hover:bg-red-100 hover:text-red-500 transition flex items-center justify-center"
                  title="删除会话">
            <el-icon><Delete /></el-icon>
          </button>
        </div>
      </div>
    </div>

    <!-- ========== 主区域 ========== -->
    <div class="flex-1 flex flex-col bg-white">
      <!-- 顶部 AI 简介栏 -->
      <div class="px-6 py-4 border-b" style="background: linear-gradient(135deg, #f3eaff 0%, #e0e7ff 100%);">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full flex items-center justify-center text-2xl"
               style="background: linear-gradient(135deg, #667eea, #764ba2);">
            🤖
          </div>
          <div>
            <h2 class="font-bold text-gray-800">Tethys AI 助手</h2>
            <p class="text-xs text-gray-500">在线 · 由 GLM-4-plus 驱动</p>
          </div>
        </div>
      </div>

      <!-- 消息列表区 -->
      <div ref="messageListRef" class="flex-1 overflow-y-auto p-6 space-y-4 bg-gray-50">
        <!-- 空状态: 示例 prompts -->
        <div v-if="messages.length === 0" class="text-center mt-20 space-y-4">
          <div class="text-6xl">👋</div>
          <p class="text-xl text-gray-700 font-semibold">你好! 我是 Tethys</p>
          <p class="text-sm text-gray-500">试试问我以下问题</p>
          <div class="grid grid-cols-2 gap-3 max-w-2xl mx-auto mt-6">
            <div v-for="prompt in samplePrompts" :key="prompt"
                 @click="inputText = prompt; handleSend()"
                 class="p-3 rounded-xl bg-white border border-gray-200 cursor-pointer hover:border-blue-400 hover:shadow-md transition text-left text-sm text-gray-600">
              {{ prompt }}
            </div>
          </div>
        </div>

        <!-- 消息列表 -->
        <div v-for="(msg, idx) in messages" :key="idx"
             :class="msg.role === 'user' ? 'flex justify-end gap-2' : 'flex justify-start gap-2'">
          <!-- AI 头像 -->
          <div v-if="msg.role === 'assistant'"
               class="w-8 h-8 rounded-full flex items-center justify-center text-lg flex-shrink-0"
               style="background: linear-gradient(135deg, #667eea, #764ba2);">
            🤖
          </div>
          <!-- 消息气泡 -->
          <div :class="msg.role === 'user'
              ? 'text-white rounded-2xl px-4 py-3 max-w-2xl shadow-md'
              : 'bg-white rounded-2xl px-4 py-3 max-w-2xl shadow-md border border-gray-100'"
               :style="msg.role === 'user' ? 'background: linear-gradient(135deg, #667eea, #764ba2);' : ''">
            <pre class="whitespace-pre-wrap font-sans text-sm leading-relaxed">{{ msg.content }}</pre>
          </div>
          <!-- 用户头像 -->
          <div v-if="msg.role === 'user'"
               class="w-8 h-8 rounded-full flex items-center justify-center text-white text-sm font-bold flex-shrink-0"
               style="background: linear-gradient(135deg, #fa709a, #fee140);">
            {{ userStore.username?.charAt(0)?.toUpperCase() }}
          </div>
        </div>

        <!-- 思考中提示 (含节点切换状态) -->
        <div v-if="loading" class="flex justify-start gap-2">
          <div class="w-8 h-8 rounded-full flex items-center justify-center text-lg flex-shrink-0"
               style="background: linear-gradient(135deg, #667eea, #764ba2);">
            🤖
          </div>
          <div class="bg-white rounded-2xl px-4 py-3 shadow-md border border-gray-100">
            <span class="text-gray-500 animate-pulse">{{ thinkingText }}</span>
          </div>
        </div>
      </div>

      <!-- 底部输入区 -->
      <div class="border-t bg-white p-4">
        <!-- Tool 切换按钮 (暂时纯 UI, Python 重构后生效) -->
        <div class="flex gap-2 mb-3 max-w-4xl mx-auto">
          <button @click="enableSearch = !enableSearch"
                  class="px-3 py-1.5 rounded-full text-sm font-medium transition flex items-center gap-1.5 border"
                  :class="enableSearch
                    ? 'text-white border-transparent'
                    : 'bg-white text-gray-600 border-gray-300 hover:border-gray-400'"
                  :style="enableSearch ? 'background: linear-gradient(135deg, #667eea, #764ba2);' : ''"
                  title="开启后 AI 可以联网搜索实时信息 (Tavily)">
            <span>🌐</span><span>联网搜索</span>
          </button>

          <button @click="enableKnowledge = !enableKnowledge"
                  class="px-3 py-1.5 rounded-full text-sm font-medium transition flex items-center gap-1.5 border"
                  :class="enableKnowledge
                    ? 'text-white border-transparent'
                    : 'bg-white text-gray-600 border-gray-300 hover:border-gray-400'"
                  :style="enableKnowledge ? 'background: linear-gradient(135deg, #4facfe, #00f2fe);' : ''"
                  title="开启后 AI 从鸣潮百科 (RAG) 查资料">
            <span>📚</span><span>知识库</span>
          </button>

          <div class="flex-1"></div>
        </div>

        <!-- 输入框 -->
        <div class="flex gap-2 max-w-4xl mx-auto">
          <el-input v-model="inputText" placeholder="问点什么... (回车发送)" size="large"
                    @keyup.enter="handleSend" :disabled="loading" />
          <el-button type="primary" size="large" @click="handleSend" :loading="loading" :icon="Promotion"
                     style="background: linear-gradient(135deg, #667eea, #764ba2); border: none;">
            发送
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Promotion, Delete } from '@element-plus/icons-vue'
import { streamChat } from '../api/ai'
import { useUserStore } from '../store/user'
import { useConversationStore } from '../store/conversation'

const userStore = useUserStore()
const store = useConversationStore()

const inputText = ref('')
const loading = ref(false)
const messageListRef = ref(null)
const abortController = ref(null)

// Tool 切换状态
const enableSearch = ref(false)
const enableKnowledge = ref(false)

// 当前会话的消息列表(来自 store)
const messages = computed(() => store.currentMessages)

// 节点切换状态 (后端 node 事件驱动)
const thinkingNode = ref('')
const thinkingText = computed(() => {
  const map = {
    retrieve_rag: '🔍 正在检索鸣潮百科...',
    llm: '🧠 正在思考...',
    tools: '🔧 正在调用工具...',
  }
  return map[thinkingNode.value] || '思考中...'
})

const samplePrompts = [
  '琳奈是什么属性? 介绍下她',
  '我有哪些角色, 推荐重点培养谁?',
  '热熔属性的5星角色都有谁?',
  '今天有什么科技新闻',
]

// 挂载: 确保有一个当前会话
onMounted(() => {
  store.ensureCurrentSession()
})

// 新对话
function handleNewChat() {
  if (loading.value) {
    abortController.value?.abort()
    loading.value = false
  }
  store.createSession()
}

// 删除会话(带确认)
async function handleDelete(id) {
  const session = store.sessions.find(s => s.id === id)
  try {
    await ElMessageBox.confirm(
      `确定删除 "${session?.title || '此会话'}" ?`,
      '删除会话',
      { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' }
    )
    store.deleteSession(id)
    ElMessage.success('已删除')
    // 如果删完没会话了, 立刻新建一个
    if (store.sessions.length === 0) {
      store.createSession()
    }
  } catch (e) {
    // 用户取消
  }
}

// 发送消息
async function handleSend() {
  const text = inputText.value.trim()
  if (!text || loading.value) return

  store.ensureCurrentSession()
  store.addMessage('user', text)
  inputText.value = ''
  scrollToBottom()

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
            // 收到第一个 token: 关掉"思考中..." 气泡, 建空 AI 消息开始追加
            firstTokenReceived = true
            loading.value = false
            store.addMessage('assistant', '')
          }
          aiContent += token
          store.updateLastMessage(aiContent)
          scrollToBottom()
        },
        onNode: (node) => {
          // 节点切换 → 更新"思考中..."文本 (只在 loading 期间生效)
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
    if (err.name === 'AbortError') {
      // 用户主动取消, 不报错
      return
    }
    ElMessage.error('AI 出错了: ' + (err.message || ''))
    loading.value = false
    thinkingNode.value = ''
  }
}

// 滚动到底
async function scrollToBottom() {
  await nextTick()
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight
  }
}

// 时间格式化(简洁)
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
