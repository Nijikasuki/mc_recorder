<template>
  <div class="min-h-screen flex items-center justify-center"
       style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
    <el-card class="w-[420px] shadow-2xl border-0" style="border-radius: 20px;">
      <template #header>
        <div class="text-center py-2">
          <div class="text-5xl mb-2">🎮</div>
          <h1 class="text-2xl font-bold"
              style="background: linear-gradient(90deg, #667eea, #764ba2); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
            鸣潮记录工具
          </h1>
          <p class="text-sm text-gray-500 mt-1">你的全能 AI 助手</p>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" @keyup.enter="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" size="large" :prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" size="large"
                    show-password :prefix-icon="Lock" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleLogin"
                     class="w-full"
                     style="background: linear-gradient(135deg, #667eea, #764ba2); border: none; font-weight: 600;">
            登 录
          </el-button>
        </el-form-item>

        <div class="text-center text-sm text-gray-500">
          还没账号? <a class="text-blue-500 cursor-pointer hover:underline">注册</a>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '../api/auth'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await login(form.username, form.password)
    if (res.code === 0) {
      userStore.setLogin(res.data, form.username)
      ElMessage.success('登录成功')
      router.push('/home')
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } catch (err) {
    ElMessage.error(err.response?.data?.message || '网络错误')
  } finally {
    loading.value = false
  }
}
</script>
