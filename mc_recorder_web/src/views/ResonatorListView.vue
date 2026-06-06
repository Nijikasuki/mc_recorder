<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <div>
        <h2 class="text-3xl font-bold text-gray-800">我的角色</h2>
        <p class="text-gray-500 mt-1">共 {{ resonators.length }} 位共鸣者</p>
      </div>
      <div class="flex gap-2">
        <el-button size="large" :icon="Connection" @click="kurobbsDialogVisible = true"
                   style="background: linear-gradient(135deg, #4facfe, #00f2fe); color: white; border: none;">
          导入鸣潮数据
        </el-button>
        <el-button type="primary" size="large" :icon="Plus" @click="openCreateDialog"
                   style="background: linear-gradient(135deg, #667eea, #764ba2); border: none;">
          新增角色
        </el-button>
      </div>
    </div>

    <!-- 顶部统计卡片 -->
    <el-row :gutter="16">
      <el-col :span="6">
        <div class="rounded-2xl bg-white p-5 shadow-sm border border-gray-100">
          <div class="text-sm text-gray-500">总角色数</div>
          <div class="text-3xl font-bold mt-1" style="color: #667eea;">{{ stats.total }}</div>
          <div class="text-xs text-gray-400 mt-2">
            库街区 {{ stats.fromKurobbs }} · 手动 {{ stats.fromManual }}
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="rounded-2xl bg-white p-5 shadow-sm border border-gray-100">
          <div class="text-sm text-gray-500">5星 / 4星</div>
          <div class="text-3xl font-bold mt-1" style="color: #f5576c;">
            {{ stats.fiveStar }} / {{ stats.fourStar }}
          </div>
          <div class="text-xs text-gray-400 mt-2">5星培养度 {{ stats.fiveStarMaxed }}/{{ stats.fiveStar }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="rounded-2xl bg-white p-5 shadow-sm border border-gray-100">
          <div class="text-sm text-gray-500">满级角色 (Lv.90)</div>
          <div class="text-3xl font-bold mt-1" style="color: #00f2fe;">{{ stats.maxLevel }}</div>
          <div class="text-xs text-gray-400 mt-2">平均等级 Lv.{{ stats.avgLevel }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="rounded-2xl bg-white p-5 shadow-sm border border-gray-100">
          <div class="text-sm text-gray-500">满链 6 链</div>
          <div class="text-3xl font-bold mt-1" style="color: #fee140;">{{ stats.fullChain }}</div>
          <div class="text-xs text-gray-400 mt-2">主角等级 Lv.{{ stats.mainRoleLevel || '-' }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 角色表格 -->
    <div class="rounded-2xl bg-white shadow-sm overflow-hidden">
      <el-table :data="resonators" v-loading="loading" stripe style="width: 100%;">
        <el-table-column label="头像" width="80">
          <template #default="{ row }">
            <el-image v-if="row.roleIconUrl"
                      :src="row.roleIconUrl"
                      style="width: 50px; height: 50px; border-radius: 8px;"
                      fit="cover" lazy />
            <div v-else class="w-[50px] h-[50px] bg-gray-100 rounded-lg flex items-center justify-center text-xs text-gray-400">
              无图
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名字" min-width="140">
          <template #default="{ row }">
            <div>
              <span class="font-semibold">{{ row.name }}</span>
              <el-tag v-if="row.isMainRole" size="small" type="warning" class="ml-1">主角</el-tag>
              <el-tag v-if="row.source === 'kurobbs'" size="small" effect="plain" class="ml-1" style="border-color:#4facfe; color:#4facfe;">库街区</el-tag>
              <el-tag v-else size="small" effect="plain" class="ml-1">手动</el-tag>
            </div>
            <div v-if="row.skinName" class="text-xs text-gray-400 mt-1">{{ row.skinName }}</div>
          </template>
        </el-table-column>
        <el-table-column label="属性" min-width="100">
          <template #default="{ row }">
            <el-tag :color="elementColors[row.element]" effect="dark" round>
              {{ row.element }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="星级" min-width="120">
          <template #default="{ row }">
            <span class="text-yellow-500">{{ '⭐'.repeat(row.star) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="等级 / 突破" min-width="200">
          <template #default="{ row }">
            <el-progress :percentage="row.level / 90 * 100"
                         :format="() => `Lv.${row.level} · ${row.breach ?? 0}/6`"
                         :stroke-width="14" />
          </template>
        </el-table-column>
        <el-table-column label="共鸣链" min-width="100">
          <template #default="{ row }">
            <el-tag type="info" effect="plain">{{ row.resonanceChain }} 链</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="武器" min-width="100">
          <template #default="{ row }">
            <el-tag v-if="row.weaponTypeName" type="warning" effect="plain">{{ row.weaponTypeName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="皮肤" width="80">
          <template #default="{ row }">
            <el-image v-if="row.skinPicUrl"
                      :src="row.skinPicUrl"
                      style="width: 50px; height: 50px; border-radius: 8px;"
                      fit="cover" lazy preview-teleported :preview-src-list="[row.skinPicUrl]" />
            <span v-else class="text-xs text-gray-300">—</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" :icon="Edit" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增/编辑 角色弹窗 -->
    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑角色' : '新增角色'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名字">
          <el-input v-model="form.name" placeholder="如: 琳奈" />
        </el-form-item>
        <el-form-item label="属性">
          <el-select v-model="form.element" placeholder="选属性" class="w-full">
            <el-option v-for="e in Object.keys(elementColors)" :key="e" :label="e" :value="e" />
          </el-select>
        </el-form-item>
        <el-form-item label="星级">
          <el-input-number v-model="form.star" :min="4" :max="5" />
        </el-form-item>
        <el-form-item label="等级">
          <el-input-number v-model="form.level" :min="1" :max="90" />
        </el-form-item>
        <el-form-item label="共鸣链">
          <el-input-number v-model="form.resonanceChain" :min="0" :max="6" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit"
                   style="background: linear-gradient(135deg, #667eea, #764ba2); border: none;">
          保存
        </el-button>
      </template>
    </el-dialog>

    <!-- 库街区导入 弹窗 -->
    <el-dialog v-model="kurobbsDialogVisible" title="导入鸣潮数据" width="600px">
      <div class="space-y-5">
        <!-- 抓 token 指引 -->
        <el-collapse>
          <el-collapse-item title="❓ 怎么拿到 b-at token? (点击展开)" name="1">
            <ol class="space-y-1 text-sm text-gray-600 list-decimal pl-5">
              <li>手机/模拟器装库街区 App 登录</li>
              <li>HTTP Toolkit 拦截 App 流量</li>
              <li>App 里点 "我的鸣潮" 触发请求</li>
              <li>找 host=api.kurobbs.com 的请求</li>
              <li>复制 Request Header 里 <code class="bg-gray-100 px-1 rounded">b-at</code> 的 value</li>
              <li>找 Body 里的 <code class="bg-gray-100 px-1 rounded">roleId</code> (玩家 ID)</li>
            </ol>
          </el-collapse-item>
        </el-collapse>

        <!-- 绑定 -->
        <div>
          <h4 class="text-base font-bold mb-3 flex items-center gap-2">
            <el-icon><Connection /></el-icon> 1. 绑定账号
          </h4>
          <el-form :model="bindForm" label-position="top">
            <el-form-item label="b-at Token">
              <el-input v-model="bindForm.batToken" placeholder="32 位 hex 字符串" />
            </el-form-item>
            <el-form-item label="鸣潮玩家 ID">
              <el-input v-model="bindForm.gameRoleId" placeholder="如 124476927" />
            </el-form-item>
            <el-button :loading="binding" @click="handleBind"
                       style="background: linear-gradient(135deg, #667eea, #764ba2); color: white; border: none;">
              提交绑定
            </el-button>
          </el-form>
        </div>

        <el-divider />

        <!-- 同步 -->
        <div>
          <h4 class="text-base font-bold mb-3 flex items-center gap-2">
            <el-icon><Refresh /></el-icon> 2. 同步角色到我的列表
          </h4>
          <p class="text-sm text-gray-600 mb-3">
            从库街区拉取你的鸣潮角色, 覆盖 source=库街区 的旧数据。手动添加的不动。
          </p>
          <div class="flex items-center gap-4">
            <el-button type="primary" :loading="syncing" @click="handleSync"
                       style="background: linear-gradient(135deg, #4facfe, #00f2fe); border: none;">
              立即同步
            </el-button>
            <div v-if="lastSyncResult" class="text-sm text-gray-600">
              ✅ 已导入 {{ lastSyncResult.count }} 个 ({{ formatTime(lastSyncResult.syncedAt) }})
            </div>
          </div>
        </div>

        <el-alert type="warning" :closable="false"
                  title="安全提示" show-icon
                  description="token 7-30 天会过期, 失效后回这重新绑定即可。请勿分享给他人。" />
      </div>
      <template #footer>
        <el-button @click="kurobbsDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Connection, Refresh } from '@element-plus/icons-vue'
import { listResonators, createResonator, updateResonator, deleteResonator } from '../api/resonator'
import { bindKurobbs, syncFromKurobbs } from '../api/kurobbs'

const elementColors = {
  '衍射': '#a78bfa',
  '热熔': '#fb923c',
  '冷凝': '#60a5fa',
  '气动': '#5eead4',
  '湮灭': '#7e22ce',
  '导电': '#facc15',
}

// ===== resonator 表格相关 =====
const resonators = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editingId = ref(null)
const form = reactive({ name: '', element: '', star: 5, level: 1, resonanceChain: 0 })

// ===== 统计数据 (computed: 角色列表变就自动重算) =====
const stats = computed(() => {
  const list = resonators.value
  const total = list.length
  if (total === 0) {
    return { total: 0, fiveStar: 0, fourStar: 0, fiveStarMaxed: 0, maxLevel: 0,
             avgLevel: 0, fullChain: 0, fromKurobbs: 0, fromManual: 0, mainRoleLevel: null }
  }
  const fiveStar = list.filter(r => r.star === 5).length
  const fourStar = list.filter(r => r.star === 4).length
  const fiveStarMaxed = list.filter(r => r.star === 5 && r.level === 90).length
  const maxLevel = list.filter(r => r.level === 90).length
  const avgLevel = Math.round(list.reduce((sum, r) => sum + (r.level || 0), 0) / total)
  const fullChain = list.filter(r => r.resonanceChain === 6).length
  const fromKurobbs = list.filter(r => r.source === 'kurobbs').length
  const fromManual = total - fromKurobbs
  const mainRole = list.find(r => r.isMainRole)
  return {
    total, fiveStar, fourStar, fiveStarMaxed, maxLevel,
    avgLevel, fullChain, fromKurobbs, fromManual,
    mainRoleLevel: mainRole ? mainRole.level : null,
  }
})

async function fetchList() {
  loading.value = true
  try {
    const res = await listResonators()
    if (res.code === 0) resonators.value = res.data || []
  } catch (err) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

function openCreateDialog() {
  editingId.value = null
  Object.assign(form, { name: '', element: '', star: 5, level: 1, resonanceChain: 0 })
  dialogVisible.value = true
}

function openEditDialog(row) {
  editingId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    if (editingId.value) {
      await updateResonator(editingId.value, form)
      ElMessage.success('更新成功')
    } else {
      await createResonator(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchList()
  } catch (err) {
    ElMessage.error(err.response?.data?.message || '操作失败')
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除 "${row.name}"?`, '提示', { type: 'warning' })
    await deleteResonator(row.id)
    ElMessage.success('删除成功')
    fetchList()
  } catch (err) {
    if (err !== 'cancel') ElMessage.error('删除失败')
  }
}

// ===== 库街区绑定 / 同步弹窗相关 =====
const kurobbsDialogVisible = ref(false)
const bindForm = reactive({ batToken: '', gameRoleId: '' })
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
      fetchList()    // ⭐ 自动刷新表格
    } else if (res.code === 401) {
      await ElMessageBox.confirm(res.message, '需要重新绑定', {
        confirmButtonText: '我知道了',
        cancelButtonText: '取消',
        type: 'warning'
      })
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
  return new Date(iso).toLocaleString('zh-CN')
}

onMounted(fetchList)
</script>
