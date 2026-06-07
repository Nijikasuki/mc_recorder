<template>
  <div class="min-h-screen flex items-center justify-center"
       style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
    <el-card class="w-[420px] shadow-2xl border-0" style="border-radius: 20px;">
      <template #header>
        <div class="text-center py-2">
          <div class="text-5xl mb-2">✨</div>
          <h1 class="text-2xl font-bold"
              style="background: linear-gradient(90deg, #667eea, #764ba2); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
            注册账号
          </h1>
          <p class="text-sm text-gray-500 mt-1">加入鸣潮记录工具</p>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" @keyup.enter="handleRegister">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" size="large" :prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码 (至少 6 位)" size="large"
                    show-password :prefix-icon="Lock" />
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" size="large"
                    show-password :prefix-icon="Lock" />
        </el-form-item>

        <el-form-item prop="email">
          <el-input v-model="form.email" placeholder="邮箱" size="large" :prefix-icon="Message" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleRegister"
                     class="w-full"
                     style="background: linear-gradient(135deg, #667eea, #764ba2); border: none; font-weight: 600;">
            注 册
          </el-button>
        </el-form-item>

        <div class="text-center text-sm text-gray-500">
          已有账号? <a class="text-blue-500 cursor-pointer hover:underline" @click="$router.push('/login')">登录</a>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message } from '@element-plus/icons-vue'
import { register } from '../api/auth'

const router = useRouter()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, message: '用户名至少 3 位', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) callback(new Error('两次密码不一致'))
        else callback()
      },
      trigger: 'blur'
    },
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不对', trigger: 'blur' },
  ],
}

async function handleRegister() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await register(form.username, form.password, form.email)
    if (res.code === 0) {
      ElMessage.success('注册成功! 请登录')
      router.push('/login')
    } else {
      ElMessage.error(res.message || '注册失败')
    }
  } catch (err) {
    ElMessage.error(err.response?.data?.message || '网络错误')
  } finally {
    loading.value = false
  }
}
</script>
