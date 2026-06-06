<template>
  <div class="space-y-6">
    <!-- 标题 -->
    <div>
      <h2 class="text-3xl font-bold text-gray-800">库街区集成</h2>
      <p class="text-gray-500 mt-1">从库街区导入你真实的鸣潮角色数据</p>
    </div>

    <!-- 步骤指引卡片 -->
    <div class="rounded-2xl bg-white p-6 shadow-sm">
      <h3 class="text-lg font-bold mb-3 flex items-center gap-2">
        <el-icon><InfoFilled /></el-icon> 怎么拿到 b-at token?
      </h3>
      <ol class="space-y-2 text-sm text-gray-600 list-decimal pl-5">
        <li>在手机/模拟器 (推荐 MuMu) 装库街区 App, 登录</li>
        <li>用 HTTP Toolkit 拦截 App 流量</li>
        <li>App 里点 "我的鸣潮", 触发 API 请求</li>
        <li>HTTP Toolkit 找 host = api.kurobbs.com 的请求</li>
        <li>从 Request Headers 复制 <code class="bg-gray-100 px-1 rounded">b-at</code> 的 value</li>
        <li>从 Body 找到 <code class="bg-gray-100 px-1 rounded">roleId</code> (是你鸣潮玩家 ID, 不是角色 ID)</li>
      </ol>
    </div>

    <!-- 绑定卡片 -->
    <div class="rounded-2xl bg-white p-6 shadow-sm">
      <h3 class="text-lg font-bold mb-4 flex items-center gap-2">
        <el-icon><Connection /></el-icon> 绑定库街区账号
      </h3>
      <el-form :model="bindForm" label-width="120px" label-position="top">
        <el-form-item label="b-at Token">
          <el-input v-model="bindForm.batToken" placeholder="从抓包工具复制的 b-at value (32 位 hex)" />
        </el-form-item>
        <el-form-item label="鸣潮玩家 ID (roleId)">
          <el-input v-model="bindForm.gameRoleId" placeholder="如 124476927" />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="binding"
            @click="handleBind"
            style="background: linear-gradient(135deg, #667eea, #764ba2); border: none;">
            绑定
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 同步卡片 -->
    <div class="rounded-2xl bg-white p-6 shadow-sm">
      <h3 class="text-lg font-bold mb-4 flex items-center gap-2">
        <el-icon><Refresh /></el-icon> 同步到我的角色表
      </h3>
      <p class="text-sm text-gray-600 mb-4">
        从库街区拉取你的鸣潮角色, 覆盖本地 source='kurobbs' 的旧数据。手动添加的角色不受影响。
      </p>
      <div class="flex items-center gap-4">
        <el-button
          type="primary"
          size="large"
          :loading="syncing"
          @click="handleSync"
          style="background: linear-gradient(135deg, #4facfe, #00f2fe); border: none;">
          立即同步
        </el-button>
        <div v-if="lastSyncResult" class="text-sm text-gray-600">
          ✅ 上次同步: {{ lastSyncResult.count }} 个角色
          <span class="text-gray-400">({{ formatTime(lastSyncResult.syncedAt) }})</span>
        </div>
      </div>
    </div>

    <!-- 提示 -->
    <el-alert
      type="warning"
      :closable="false"
      title="安全提示"
      description="token 7-30 天会过期, 失效后到这里重新绑定即可。请勿把 token 分享给他人。"
      show-icon
    />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { InfoFilled, Connection, Refresh } from '@element-plus/icons-vue'
import { bindKurobbs, syncFromKurobbs } from '../api/kurobbs'

const bindForm = reactive({
  batToken: '',
  gameRoleId: '',
})
const binding = ref(false)
const syncing = ref(false)
const lastSyncResult = ref(null)

async function handleBind() {
  if (!bindForm.batToken.trim() || !bindForm.gameRoleId.trim()) {
    ElMessage.warning('请填完 token 和玩家 ID')
    return
  }
  binding.value = true
  try {
    const res = await bindKurobbs(bindForm.batToken.trim(), bindForm.gameRoleId.trim())
    if (res.code === 0) {
      ElMessage.success('绑定成功! 可以同步了')
      bindForm.batToken = ''
    } else {
      ElMessage.error(res.message || '绑定失败')
    }
  } catch (err) {
    ElMessage.error(err.response?.data?.message || err.message || '网络错误')
  } finally {
    binding.value = false
  }
}

async function handleSync() {
  syncing.value = true
  try {
    const res = await syncFromKurobbs()
    if (res.code === 0) {
      lastSyncResult.value = res.data
      ElMessage.success(`同步成功! 共导入 ${res.data.count} 个角色`)
    } else if (res.code === 401) {
      // 业务级 401: token 失效, 引导重新绑定
      await ElMessageBox.confirm(
        res.message,
        '需要重新绑定',
        { confirmButtonText: '去绑定', cancelButtonText: '取消', type: 'warning' }
      )
      // 用户确认后, 滚动到绑定区域 (页面上半部)
      window.scrollTo({ top: 0, behavior: 'smooth' })
    } else {
      ElMessage.error(res.message || '同步失败')
    }
  } catch (err) {
    ElMessage.error(err.response?.data?.message || err.message || '网络错误')
  } finally {
    syncing.value = false
  }
}

function formatTime(iso) {
  if (!iso) return ''
  const d = new Date(iso)
  return d.toLocaleString('zh-CN')
}
</script>
