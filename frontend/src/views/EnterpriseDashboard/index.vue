<template>
  <div class="enterprise-dashboard">
    <AppHeader />
    <div class="container mt-20">
      <h2 class="mb-20">企业工作台</h2>

      <el-row :gutter="20" class="mb-20">
        <el-col :span="6" v-for="metric in metrics" :key="metric.label">
          <el-card shadow="hover" class="metric-card text-center">
            <div class="metric-value">{{ metric.value }}</div>
            <div class="metric-label">{{ metric.label }}</div>
          </el-card>
        </el-col>
      </el-row>

      <el-card>
        <template #header>
          <div class="flex-between">
            <span>我的订单</span>
            <el-button type="primary" link @click="$router.push('/orders')">查看全部</el-button>
          </div>
        </template>

        <el-tabs v-model="activeTab">
          <el-tab-pane label="全部" name="all" />
          <el-tab-pane label="待付定金" name="0" />
          <el-tab-pane label="开发中" name="2" />
          <el-tab-pane label="待验收" name="4" />
          <el-tab-pane label="待付尾款" name="5" />
          <el-tab-pane label="已完成" name="6" />
        </el-tabs>

        <el-table :data="orders" stripe>
          <el-table-column prop="orderNo" label="订单号" width="200" />
          <el-table-column prop="serviceTitle" label="服务项目" min-width="200" />
          <el-table-column label="定金" width="120">
            <template #default="{ row }">¥{{ row.depositAmount }}</template>
          </el-table-column>
          <el-table-column label="尾款" width="120">
            <template #default="{ row }">¥{{ row.finalAmount }}</template>
          </el-table-column>
          <el-table-column label="总金额" width="120">
            <template #default="{ row }">¥{{ row.totalAmount }}</template>
          </el-table-column>
          <el-table-column prop="statusText" label="状态" width="120">
            <template #default="{ row }">
              <el-tag :type="statusTagType(row.status)" size="small">{{ row.statusText }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button type="primary" link size="small" @click="$router.push(`/order/${row.id}`)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const metrics = ref([
  { label: '进行中订单', value: 5 },
  { label: '累计支出', value: '¥128,000' },
  { label: '待付定金', value: 2 },
  { label: '待付尾款', value: 1 }
])

const activeTab = ref('all')

const orders = ref([
  { id: 1001, orderNo: 'ORD20240401001', serviceTitle: '企业官网开发', depositAmount: 4500, finalAmount: 10500, totalAmount: 15000, status: 2, statusText: '开发中' },
  { id: 1002, orderNo: 'ORD20240401002', serviceTitle: '品牌VI设计', depositAmount: 2400, finalAmount: 5600, totalAmount: 8000, status: 0, statusText: '待付定金' },
  { id: 1003, orderNo: 'ORD20240401003', serviceTitle: '小程序开发', depositAmount: 7500, finalAmount: 17500, totalAmount: 25000, status: 5, statusText: '待付尾款' },
  { id: 1004, orderNo: 'ORD20240401004', serviceTitle: 'SEO优化', depositAmount: 1500, finalAmount: 3500, totalAmount: 5000, status: 6, statusText: '已完成' }
])

function statusTagType(status) {
  const map = { 0: 'info', 1: 'warning', 2: '', 3: 'success', 4: 'warning', 5: 'danger', 6: 'success', 7: 'info' }
  return map[status] || 'info'
}
</script>

<style scoped lang="scss">
.metric-card .metric-value {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
}

.metric-card .metric-label {
  color: #909399;
  margin-top: 4px;
}
</style>
