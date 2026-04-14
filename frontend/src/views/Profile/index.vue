<template>
  <div class="profile-page">
    <AppHeader />
    <div class="container mt-20">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="mb-20">
            <div class="profile-header text-center">
              <el-avatar :size="80" :src="userInfo.avatar">{{ userInfo.name?.[0] || 'U' }}</el-avatar>
              <h3 class="mt-10">{{ userInfo.name }}</h3>
              <el-tag type="warning" size="large">L{{ userInfo.level || 1 }}</el-tag>
              <div class="profile-stats mt-20">
                <div class="stat-item">
                  <div class="stat-value">{{ userInfo.orderCount || 0 }}</div>
                  <div class="stat-label">订单</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ userInfo.serviceCount || 0 }}</div>
                  <div class="stat-label">服务</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{{ userInfo.rating || 0 }}</div>
                  <div class="stat-label">评分</div>
                </div>
              </div>
            </div>
          </el-card>

          <el-card>
            <template #header>账户信息</template>
            <el-descriptions :column="1">
              <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
              <el-descriptions-item label="角色">{{ userInfo.role === 'opc' ? 'OPC' : '企业' }}</el-descriptions-item>
              <el-descriptions-item label="手机">{{ userInfo.phone || '-' }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ userInfo.email || '-' }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>

        <el-col :span="16">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="我的订单" name="orders">
                <el-table :data="orders" stripe>
                  <el-table-column prop="orderNo" label="订单号" />
                  <el-table-column prop="serviceTitle" label="服务" />
                  <el-table-column prop="totalAmount" label="金额" width="100">
                    <template #default="{ row }">¥{{ row.totalAmount }}</template>
                  </el-table-column>
                  <el-table-column prop="statusText" label="状态" width="100">
                    <template #default="{ row }">
                      <el-tag size="small" :type="statusTagType(row.status)">{{ row.statusText }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80">
                    <template #default="{ row }">
                      <el-button type="primary" link size="small" @click="$router.push(`/order/${row.id}`)">详情</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
              <el-tab-pane label="我的服务" name="services">
                <el-table :data="services" stripe>
                  <el-table-column prop="title" label="服务名称" />
                  <el-table-column prop="price" label="价格" width="100">
                    <template #default="{ row }">¥{{ row.price }}</template>
                  </el-table-column>
                  <el-table-column prop="sales" label="销量" width="80" />
                </el-table>
              </el-tab-pane>
              <el-tab-pane label="账户设置" name="settings">
                <el-form label-width="100px">
                  <el-form-item label="头像">
                    <el-upload action="/api/upload" :limit="1" list-type="picture-card">
                      <el-icon><Plus /></el-icon>
                    </el-upload>
                  </el-form-item>
                  <el-form-item label="昵称">
                    <el-input v-model="settingsForm.name" />
                  </el-form-item>
                  <el-form-item label="手机号">
                    <el-input v-model="settingsForm.phone" />
                  </el-form-item>
                  <el-form-item label="邮箱">
                    <el-input v-model="settingsForm.email" />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary">保存修改</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const userStore = useUserStore()
const activeTab = ref('orders')

const userInfo = reactive({
  name: userStore.userInfo?.name || '用户',
  username: userStore.userInfo?.username || '',
  avatar: userStore.userInfo?.avatar || 'https://modao.cc/agent-py/media/generated_images/2026-03-30/2ec52e2674fe4a148fc677ca5bfd9898.jpg',
  level: userStore.userInfo?.level || 1,
  role: userStore.role,
  phone: userStore.userInfo?.phone || '',
  email: userStore.userInfo?.email || '',
  orderCount: 12,
  serviceCount: 5,
  rating: 4.8
})

const settingsForm = reactive({
  name: userInfo.name,
  phone: userInfo.phone,
  email: userInfo.email
})

const orders = ref([
  { id: 1, orderNo: 'ORD20240401001', serviceTitle: '企业官网开发', totalAmount: 15000, status: 2, statusText: '开发中' },
  { id: 2, orderNo: 'ORD20240401002', serviceTitle: '品牌VI设计', totalAmount: 8000, status: 6, statusText: '已完成' }
])

const services = ref([
  { id: 1, title: '企业官网设计与开发', price: 15000, sales: 128 },
  { id: 2, title: '小程序全栈开发', price: 25000, sales: 215 }
])

function statusTagType(status) {
  const map = { 0: 'info', 1: 'warning', 2: '', 3: 'success', 4: 'warning', 5: 'danger', 6: 'success', 7: 'info' }
  return map[status] || 'info'
}
</script>

<style scoped lang="scss">
.profile-stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  color: #909399;
  font-size: 12px;
  margin-top: 4px;
}
</style>
