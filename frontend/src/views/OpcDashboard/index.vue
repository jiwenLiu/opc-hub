<template>
  <div class="opc-dashboard">
    <AppHeader />
    <div class="container mt-20">
      <h2 class="mb-20">OPC 工作台</h2>

      <el-row :gutter="20" class="mb-20">
        <el-col :span="6" v-for="metric in metrics" :key="metric.label">
          <el-card shadow="hover" class="metric-card text-center">
            <div class="metric-value">{{ metric.value }}</div>
            <div class="metric-label">{{ metric.label }}</div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="16">
          <el-card>
            <template #header>
              <div class="flex-between">
                <span>最近订单</span>
                <el-button type="primary" link @click="$router.push('/orders')">查看全部</el-button>
              </div>
            </template>
            <el-table :data="recentOrders" stripe>
              <el-table-column prop="orderNo" label="订单号" />
              <el-table-column prop="serviceTitle" label="服务项目" />
              <el-table-column prop="totalAmount" label="总金额" width="120">
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
        </el-col>

        <el-col :span="8">
          <el-card class="mb-20">
            <template #header>待办事项</template>
            <div v-for="todo in todos" :key="todo.id" class="todo-item">
              <el-tag :type="todo.type" size="small">{{ todo.tag }}</el-tag>
              <span class="ml-10">{{ todo.text }}</span>
            </div>
          </el-card>

          <el-card>
            <template #header>等级进度</template>
            <div class="level-info">
              <div class="flex-between mb-10">
                <span>当前等级: <el-tag type="warning">L{{ currentLevel }}</el-tag></span>
                <span>{{ currentExp }}/{{ nextLevelExp }} EXP</span>
              </div>
              <el-progress :percentage="levelPercent" :stroke-width="12" />
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const metrics = ref([
  { label: '进行中订单', value: 12 },
  { label: '本月收入', value: '¥45,800' },
  { label: '服务好评率', value: '98.5%' },
  { label: '累计完成', value: 156 }
])

const recentOrders = ref([
  { id: 1001, orderNo: 'ORD20240401001', serviceTitle: '企业官网开发', totalAmount: 15000, status: 2, statusText: '开发中' },
  { id: 1002, orderNo: 'ORD20240401002', serviceTitle: '品牌VI设计', totalAmount: 8000, status: 3, statusText: '已交付' },
  { id: 1003, orderNo: 'ORD20240401003', serviceTitle: '小程序开发', totalAmount: 25000, status: 1, statusText: '待开发' }
])

const todos = ref([
  { id: 1, tag: '紧急', type: 'danger', text: '订单 #1001 需要在明天前交付' },
  { id: 2, tag: '待处理', type: 'warning', text: '订单 #1002 等待客户验收' },
  { id: 3, tag: '新消息', type: 'info', text: '客户李总发来新消息' }
])

const currentLevel = ref(4)
const currentExp = ref(3200)
const nextLevelExp = ref(5000)

const levelPercent = computed(() => Math.round((currentExp.value / nextLevelExp.value) * 100))

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

.todo-item {
  padding: 10px 0;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  align-items: center;

  &:last-child {
    border-bottom: none;
  }
}

.ml-10 {
  margin-left: 10px;
}
</style>
