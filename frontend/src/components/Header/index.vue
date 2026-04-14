<template>
  <header class="site-header">
    <div class="container flex-between">
      <div class="logo" @click="$router.push('/')">
        <el-icon :size="28" color="#409EFF"><Cpu /></el-icon>
        <span>OPC Hub</span>
      </div>

      <nav class="nav-menu">
        <router-link to="/" class="nav-item">首页</router-link>
        <router-link to="/tools" class="nav-item">工具市场</router-link>
        <router-link to="/match" class="nav-item" v-if="isLoggedIn">智能匹配</router-link>
        <router-link to="/orders" class="nav-item" v-if="isLoggedIn">我的订单</router-link>
      </nav>

      <div class="user-area">
        <template v-if="isLoggedIn">
          <el-dropdown trigger="click" @command="handleCommand">
            <div class="user-info flex-center">
              <el-avatar :size="32">{{ userInfo?.name?.[0] || 'U' }}</el-avatar>
              <span class="ml-10">{{ userInfo?.name || '用户' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="dashboard">
                  {{ role === 'opc' ? 'OPC工作台' : '企业工作台' }}
                </el-dropdown-item>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="message">消息中心</el-dropdown-item>
                <el-dropdown-item divided command="switchRole">
                  切换为{{ role === 'opc' ? '企业' : 'OPC' }}
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const isLoggedIn = computed(() => userStore.isLoggedIn)
const userInfo = computed(() => userStore.userInfo)
const role = computed(() => userStore.role)

function handleCommand(command) {
  switch (command) {
    case 'dashboard':
      router.push(role.value === 'opc' ? '/opc/dashboard' : '/enterprise/dashboard')
      break
    case 'profile':
      router.push('/profile')
      break
    case 'message':
      router.push('/message')
      break
    case 'switchRole':
      userStore.setRole(role.value === 'opc' ? 'employer' : 'opc')
      ElMessage.success(`已切换为${role.value === 'opc' ? 'OPC' : '企业'}角色`)
      router.push(role.value === 'opc' ? '/opc/dashboard' : '/enterprise/dashboard')
      break
    case 'logout':
      userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/')
      break
  }
}
</script>

<style scoped lang="scss">
.site-header {
  height: 60px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}

.nav-menu {
  display: flex;
  gap: 24px;
}

.nav-item {
  color: #606266;
  font-size: 15px;
  padding: 8px 0;
  position: relative;

  &:hover,
  &.router-link-active {
    color: #409EFF;
  }

  &.router-link-active::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 2px;
    background: #409EFF;
  }
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.ml-10 {
  margin-left: 10px;
}
</style>
