<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <div>
        <h2 class="text-3xl font-bold text-gray-800">我的角色</h2>
        <p class="text-gray-500 mt-1">共 {{ resonators.length }} 位共鸣者</p>
      </div>
      <el-button type="primary" size="large" :icon="Plus" @click="openCreateDialog"
                 style="background: linear-gradient(135deg, #667eea, #764ba2); border: none;">
        新增角色
      </el-button>
    </div>

    <div class="rounded-2xl bg-white shadow-sm overflow-hidden">
      <el-table :data="resonators" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="名字">
          <template #default="{ row }">
            <span class="font-semibold">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="属性">
          <template #default="{ row }">
            <el-tag :color="elementColors[row.element]" effect="dark" round>
              {{ row.element }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="星级" width="120">
          <template #default="{ row }">
            <span class="text-yellow-500 text-base">
              {{ '⭐'.repeat(row.star) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="等级" width="160">
          <template #default="{ row }">
            <el-progress :percentage="row.level / 90 * 100" :format="() => `Lv.${row.level}`"
                         :stroke-width="14" />
          </template>
        </el-table-column>
        <el-table-column prop="resonanceChain" label="共鸣链" width="100">
          <template #default="{ row }">
            <el-tag type="info" effect="plain">{{ row.resonanceChain }} 链</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" :icon="Edit" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { listResonators, createResonator, updateResonator, deleteResonator } from '../api/resonator'

// 属性配色: 衍射紫 / 热熔橙 / 冷凝蓝 / 气动青 / 湮灭紫黑 / 导电黄
const elementColors = {
  '衍射': '#a78bfa',
  '热熔': '#fb923c',
  '冷凝': '#60a5fa',
  '气动': '#5eead4',
  '湮灭': '#7e22ce',
  '导电': '#facc15',
}

const resonators = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const editingId = ref(null)
const form = reactive({ name: '', element: '', star: 5, level: 1, resonanceChain: 0 })

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

onMounted(fetchList)
</script>
