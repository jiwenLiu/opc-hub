<template>
  <div class="login-page">
    <AppHeader />
    <div class="login-container">
      <el-card class="login-card">
        <template #header>
          <h2 class="text-center">用户登录</h2>
        </template>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="80px"
          @submit.prevent="handleLogin"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" class="full-width" @click="handleLogin">
              登录
            </el-button>
          </el-form-item>
        </el-form>
        <div class="text-center mt-10">
          <span>还没有账号？</span>
          <router-link to="/register" class="link">立即注册</router-link>
        </div>
      </el-card>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await userStore.login(form)
    ElMessage.success('登录成功')
    const role = res.data.role || 'employer'
    const redirect = route.query.redirect || (role === 'opc' ? '/opc/dashboard' : '/enterprise/dashboard')
    router.replace(redirect)
  } catch (e) {
    console.error('登录失败:', e)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-container {
  min-height: calc(100vh - 160px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
}

.login-card {
  width: 420px;
}

.full-width {
  width: 100%;
}

.link {
  color: #409EFF;
}
</style>
