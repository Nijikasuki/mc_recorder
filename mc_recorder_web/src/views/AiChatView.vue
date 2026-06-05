<template>
  <div class="flex flex-col rounded-2xl overflow-hidden bg-white shadow-sm"
       style="height: calc(100vh - 112px);">

    <!-- 顶部 AI 简介栏 -->
    <div class="px-6 py-4 border-b" style="background: linear-gradient(135deg, #f3eaff 0%, #e0e7ff 100%);">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-full flex items-center justify-center text-2xl"
             style="background: linear-gradient(135deg, #667eea, #764ba2);">
          🤖
        </div>
        <div>
          <h2 class="font-bold text-gray-800">鸣潮 AI 助手</h2>
          <p class="text-xs text-gray-500">在线 · 由 GLM-4-plus 驱动</p>
        </div>
      </div>
    </div>

    <!-- 消息列表区 -->
    <div ref="messageListRef" class="flex-1 overflow-y-auto p-6 space-y-4 bg-gray-50">
      <!-- 空状态: 示例 prompts -->
      <div v-if="messages.length === 0" class="text-center mt-20 space-y-4">
        <div class="text-6xl">👋</div>
        <p class="text-xl text-gray-700 font-semibold">你好! 我是鸣潮助手</p>
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

      <!-- 思考中提示 -->
      <div v-if="loading" class="flex justify-start gap-2">
        <div class="w-8 h-8 rounded-full flex items-center justify-center text-lg flex-shrink-0"
             style="background: linear-gradient(135deg, #667eea, #764ba2);">
          🤖
        </div>
        <div class="bg-white rounded-2xl px-4 py-3 shadow-md border border-gray-100">
          <span class="text-gray-500 animate-pulse">思考中...</span>
        </div>
      </div>
    </div>

    <!-- 输入框区 -->
    <div class="border-t bg-white p-4">
      <div class="flex gap-2 max-w-4xl mx-auto">
        <el-input v-model="inputText" placeholder="问点什么... (回车发送)" size="large"
                  @keyup.enter="handleSend" :disabled="loading" />
        <el-button type="primary" size="large" @click="handleSend" :loading="loading" :icon="Promotion"
                   style="background: linear-gradient(135deg, #667eea, #764ba2); border: none;">
          发送
        </el-button>
        <el-button size="large" :icon="Delete" @click="handleClear">清空</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Promotion, Delete } from '@element-plus/icons-vue'
import { streamChat } from '../api/ai'
import { useUserStore } from '../store/user'

const userStore = useUserStore()
const messages = ref([])
const inputText = ref('')
const loading = ref(false)
const messageListRef = ref(null)

const samplePrompts = [
  '琳奈是什么属性? 介绍下她',
  '我有哪些角色, 推荐重点培养谁?',
  '热熔属性的5星角色都有谁?',
  '今天有什么科技新闻',
]

async function handleSend() {
  const text = inputText.value.trim()
  if (!text || loading.value) return

  messages.value.push({ role: 'user', content: text })
  inputText.value = ''
  scrollToBottom()

  loading.value = true
  try {
    // 1. 等流式响应收齐 (期间只显示"思考中", 没有空气泡)
    let fullText = ''
    await streamChat(text, (chunk) => {
      fullText += chunk
    })

    // 2. 收到内容后再 push AI 气泡, 立刻开始打字
    const aiMsg = reactive({ role: 'assistant', content: '' })
    messages.value.push(aiMsg)
    loading.value = false   // 先关掉"思考中", 再开始打字效果
    await typewriterEffect(aiMsg, fullText, 20)
  } catch (err) {
    ElMessage.error('AI 出错了: ' + err.message)
    loading.value = false
  }
}

async function typewriterEffect(msg, text, delay = 20) {
  for (const char of text) {
    msg.content += char
    scrollToBottom()
    await new Promise(resolve => setTimeout(resolve, delay))
  }
}

function handleClear() {
  messages.value = []
  ElMessage.success('已清空')
}

async function scrollToBottom() {
  await nextTick()
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight
  }
}
</script>
