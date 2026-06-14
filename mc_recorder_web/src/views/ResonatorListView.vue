<template>
  <div class="space-y-6">
    <!-- 标题 + 操作按钮 -->
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-[22px] font-semibold text-[#E6E6E8]">我的角色</h2>
        <p class="mt-1 text-[13px] text-[#8B949E]">共 {{ resonators.length }} 位共鸣者</p>
      </div>
      <div class="flex gap-2">
        <button
          @click="kurobbsDialogVisible = true"
          class="flex items-center gap-1.5 rounded-xl bg-gradient-to-r from-cyan-400 to-blue-500 px-4 py-2 text-[13px] font-medium text-white shadow-lg shadow-blue-500/20 transition-all hover:brightness-110 active:scale-[0.98]"
        >
          <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M4 16v2a2 2 0 002 2h12a2 2 0 002-2v-2M7 10l5 5 5-5M12 15V3" />
          </svg>
          导入鸣潮数据
        </button>
        <button
          @click="openCreateDialog"
          class="flex items-center gap-1.5 rounded-xl bg-gradient-to-r from-violet-500 to-purple-600 px-4 py-2 text-[13px] font-medium text-white shadow-lg shadow-purple-500/20 transition-all hover:brightness-110 active:scale-[0.98]"
        >
          <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4" />
          </svg>
          新增角色
        </button>
      </div>
    </div>

    <!-- 顶部统计卡片 -->
    <div class="grid grid-cols-2 gap-4 md:grid-cols-4">
      <div class="rounded-2xl border border-[#1F242C] bg-[#161B22] p-5 transition-colors hover:border-[#2A3038]">
        <div class="text-[12px] text-[#8B949E]">总角色数</div>
        <div class="mt-1 text-[28px] font-bold text-violet-400">{{ stats.total }}</div>
        <div class="mt-2 text-[11px] text-[#484F58]">
          库街区 {{ stats.fromKurobbs }} · 手动 {{ stats.fromManual }}
        </div>
      </div>
      <div class="rounded-2xl border border-[#1F242C] bg-[#161B22] p-5 transition-colors hover:border-[#2A3038]">
        <div class="text-[12px] text-[#8B949E]">5星 / 4星</div>
        <div class="mt-1 text-[28px] font-bold text-rose-400">
          {{ stats.fiveStar }} / {{ stats.fourStar }}
        </div>
        <div class="mt-2 text-[11px] text-[#484F58]">5星培养度 {{ stats.fiveStarMaxed }}/{{ stats.fiveStar }}</div>
      </div>
      <div class="rounded-2xl border border-[#1F242C] bg-[#161B22] p-5 transition-colors hover:border-[#2A3038]">
        <div class="text-[12px] text-[#8B949E]">满级角色 (Lv.90)</div>
        <div class="mt-1 text-[28px] font-bold text-cyan-400">{{ stats.maxLevel }}</div>
        <div class="mt-2 text-[11px] text-[#484F58]">平均等级 Lv.{{ stats.avgLevel }}</div>
      </div>
      <div class="rounded-2xl border border-[#1F242C] bg-[#161B22] p-5 transition-colors hover:border-[#2A3038]">
        <div class="text-[12px] text-[#8B949E]">满链 6 链</div>
        <div class="mt-1 text-[28px] font-bold text-amber-400">{{ stats.fullChain }}</div>
        <div class="mt-2 text-[11px] text-[#484F58]">主角等级 Lv.{{ stats.mainRoleLevel || '-' }}</div>
      </div>
    </div>

    <!-- 角色表格 -->
    <div class="overflow-hidden rounded-2xl border border-[#1F242C] bg-[#161B22] dark-table">
      <el-table :data="resonators" v-loading="loading" style="width: 100%;">
        <el-table-column label="头像" width="80">
          <template #default="{ row }">
            <el-image v-if="row.roleIconUrl"
                      :src="row.roleIconUrl"
                      style="width: 44px; height: 44px; border-radius: 8px;"
                      fit="cover" lazy />
            <div v-else class="flex h-11 w-11 items-center justify-center rounded-lg bg-[#0D1117] text-[10px] text-[#484F58]">
              无图
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名字" min-width="140">
          <template #default="{ row }">
            <div class="text-[#E6E6E8]">
              <span class="font-medium">{{ row.name }}</span>
              <span v-if="row.isMainRole" class="ml-1.5 rounded-md bg-amber-500/15 px-1.5 py-0.5 text-[10px] font-medium text-amber-400">主角</span>
              <span v-if="row.source === 'kurobbs'" class="ml-1 rounded-md bg-cyan-500/15 px-1.5 py-0.5 text-[10px] font-medium text-cyan-400">库街区</span>
              <span v-else class="ml-1 rounded-md bg-[#1F242C] px-1.5 py-0.5 text-[10px] font-medium text-[#8B949E]">手动</span>
            </div>
            <div v-if="row.skinName" class="mt-1 text-[11px] text-[#8B949E]">{{ row.skinName }}</div>
          </template>
        </el-table-column>
        <el-table-column label="属性" min-width="100">
          <template #default="{ row }">
            <span
              class="rounded-full px-2.5 py-1 text-[11px] font-medium text-white"
              :style="{ background: elementColors[row.element] }"
            >
              {{ row.element }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="星级" min-width="120">
          <template #default="{ row }">
            <span class="text-amber-400">{{ '★'.repeat(row.star) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="等级 / 突破" min-width="200">
          <template #default="{ row }">
            <div class="space-y-1">
              <div class="flex justify-between text-[11px] text-[#8B949E]">
                <span>Lv.{{ row.level }}</span>
                <span>{{ row.breach ?? 0 }}/6</span>
              </div>
              <div class="h-1.5 overflow-hidden rounded-full bg-[#0D1117]">
                <div
                  class="h-full rounded-full bg-gradient-to-r from-violet-500 to-purple-500"
                  :style="{ width: `${row.level / 90 * 100}%` }"
                ></div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="共鸣链" min-width="100">
          <template #default="{ row }">
            <span class="rounded-md border border-[#1F242C] bg-[#0D1117] px-2 py-0.5 text-[11px] text-[#8B949E]">
              {{ row.resonanceChain }} 链
            </span>
          </template>
        </el-table-column>
        <el-table-column label="武器" min-width="100">
          <template #default="{ row }">
            <span v-if="row.weaponTypeName" class="rounded-md bg-amber-500/15 px-2 py-0.5 text-[11px] text-amber-400">
              {{ row.weaponTypeName }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="皮肤" width="80">
          <template #default="{ row }">
            <el-image v-if="row.skinPicUrl"
                      :src="row.skinPicUrl"
                      style="width: 44px; height: 44px; border-radius: 8px;"
                      fit="cover" lazy preview-teleported :preview-src-list="[row.skinPicUrl]" />
            <span v-else class="text-[11px] text-[#484F58]">—</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="170" fixed="right">
          <template #default="{ row }">
            <button
              @click="openEditDialog(row)"
              class="mr-1 rounded-md border border-[#1F242C] bg-[#0D1117] px-2.5 py-1 text-[11px] text-[#8B949E] transition-colors hover:border-violet-500/40 hover:text-violet-400"
            >
              编辑
            </button>
            <button
              @click="handleDelete(row)"
              class="rounded-md border border-rose-500/30 bg-rose-500/10 px-2.5 py-1 text-[11px] text-rose-400 transition-colors hover:bg-rose-500/20"
            >
              删除
            </button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增/编辑 角色弹窗 -->
    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑角色' : '新增角色'" width="500px" class="dark-dialog">
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
        <button @click="dialogVisible = false" class="mr-2 rounded-lg border border-[#1F242C] bg-[#0D1117] px-4 py-2 text-[13px] text-[#8B949E] hover:text-[#E6E6E8]">取消</button>
        <button @click="handleSubmit" class="rounded-lg bg-gradient-to-r from-violet-500 to-purple-600 px-4 py-2 text-[13px] font-medium text-white shadow-lg shadow-purple-500/20 hover:brightness-110">保存</button>
      </template>
    </el-dialog>

    <!-- 库街区导入 弹窗 -->
    <el-dialog v-model="kurobbsDialogVisible" title="导入鸣潮数据" width="600px" class="dark-dialog">
      <div class="space-y-5">
        <el-collapse>
          <el-collapse-item title="❓ 怎么拿到 b-at token? (点击展开)" name="1">
            <ol class="space-y-1 list-decimal pl-5 text-[12px] text-[#8B949E]">
              <li>手机/模拟器装库街区 App 登录</li>
              <li>HTTP Toolkit 拦截 App 流量</li>
              <li>App 里点 "我的鸣潮" 触发请求</li>
              <li>找 host=api.kurobbs.com 的请求</li>
              <li>复制 Request Header 里 <code class="rounded bg-[#1F242C] px-1 text-amber-400">b-at</code> 的 value</li>
              <li>找 Body 里的 <code class="rounded bg-[#1F242C] px-1 text-amber-400">roleId</code> (玩家 ID)</li>
            </ol>
          </el-collapse-item>
        </el-collapse>

        <div>
          <h4 class="mb-3 text-[14px] font-semibold text-[#E6E6E8]">1. 绑定账号</h4>
          <el-form :model="bindForm" label-position="top">
            <el-form-item label="b-at Token">
              <el-input v-model="bindForm.batToken" placeholder="32 位 hex 字符串" />
            </el-form-item>
            <el-form-item label="鸣潮玩家 ID">
              <el-input v-model="bindForm.gameRoleId" placeholder="如 124476927" />
            </el-form-item>
            <button @click="handleBind" :disabled="binding" class="rounded-lg bg-gradient-to-r from-violet-500 to-purple-600 px-4 py-2 text-[13px] font-medium text-white shadow-lg shadow-purple-500/20 hover:brightness-110 disabled:opacity-50">
              {{ binding ? '绑定中...' : '提交绑定' }}
            </button>
          </el-form>
        </div>

        <div class="h-px bg-[#1F242C]"></div>

        <div>
          <h4 class="mb-3 text-[14px] font-semibold text-[#E6E6E8]">2. 同步角色到我的列表</h4>
          <p class="mb-3 text-[12px] text-[#8B949E]">
            从库街区拉取你的鸣潮角色, 覆盖 source=库街区 的旧数据。手动添加的不动。
          </p>
          <div class="flex items-center gap-3">
            <button @click="handleSync" :disabled="syncing" class="rounded-lg bg-gradient-to-r from-cyan-400 to-blue-500 px-4 py-2 text-[13px] font-medium text-white shadow-lg shadow-blue-500/20 hover:brightness-110 disabled:opacity-50">
              {{ syncing ? '同步中...' : '立即同步' }}
            </button>
            <div v-if="lastSyncResult" class="text-[12px] text-emerald-400">
              已导入 {{ lastSyncResult.count }} 个 ({{ formatTime(lastSyncResult.syncedAt) }})
            </div>
          </div>
        </div>

        <div class="flex gap-2 rounded-xl border border-amber-500/30 bg-amber-500/10 p-3">
          <svg class="h-4 w-4 flex-shrink-0 text-amber-400" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2L1 21h22L12 2zm0 6l7.5 13H4.5L12 8zm-1 4v4h2v-4h-2zm0 6v2h2v-2h-2z" />
          </svg>
          <div class="text-[12px] text-amber-200">
            <div class="font-medium">安全提示</div>
            <div class="mt-0.5 text-amber-300/80">token 7-30 天会过期, 失效后回这重新绑定即可。请勿分享给他人。</div>
          </div>
        </div>
      </div>
      <template #footer>
        <button @click="kurobbsDialogVisible = false" class="rounded-lg border border-[#1F242C] bg-[#0D1117] px-4 py-2 text-[13px] text-[#8B949E] hover:text-[#E6E6E8]">关闭</button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listResonators, createResonator, updateResonator, deleteResonator } from '../api/resonator'
import { bindKurobbs, syncFromKurobbs } from '../api/kurobbs'

const elementColors = {
  '衍射': 'linear-gradient(135deg, #a78bfa, #8b5cf6)',
  '热熔': 'linear-gradient(135deg, #fb923c, #f97316)',
  '冷凝': 'linear-gradient(135deg, #60a5fa, #3b82f6)',
  '气动': 'linear-gradient(135deg, #5eead4, #14b8a6)',
  '湮灭': 'linear-gradient(135deg, #c084fc, #9333ea)',
  '导电': 'linear-gradient(135deg, #facc15, #eab308)',
}

const resonators = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editingId = ref(null)
const form = reactive({ name: '', element: '', star: 5, level: 1, resonanceChain: 0 })

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
      fetchList()
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
