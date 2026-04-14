<template>
  <div class="order-list-page">
    <AppHeader />
    <div class="container mt-20">
      <h2 class="mb-20">订单列表</h2>

      <el-card>
        <el-tabs v-model="activeTab" @tab-change="handleTabChange">
          <el-tab-pane label="全部" name="all" />
          <el-tab-pane label="待付定金" name="0" />
          <el-tab-pane label="待开发" name="1" />
          <el-tab-pane label="开发中" name="2" />
          <el-tab-pane label="已交付" name="3" />
          <el-tab-pane label="验收中" name="4" />
          <el-tab-pane label="待付尾款" name="5" />
          <el-tab-pane label="已完成" name="6" />
          <el-tab-pane label="已取消" name="7" />
        </el-tabs>

        <div v-if="orders.length === 0" class="text-center" style="padding: 40px">
          <el-empty description="暂无订单" />
        </div>

        <div v-else>
          <el-card v-for="order in orders" :key="order.id" shadow="hover" class="order-card">
            <div class="order-header flex-between">
              <div>
                <span class="order-no">{{ order.orderNo }}</span>
                <el-tag :type="statusTagType(order.status)" size="small" class="ml-10">{{ order.statusText }}</el-tag>
              </div>
              <span class="text-muted">{{ order.createdAt }}</span>
            </div>
            <div class="order-body mt-10">
              <div class="order-title">{{ order.serviceTitle }}</div>
              <div class="order-amounts mt-10">
                <span>总金额: <strong class="text-primary">¥{{ order.totalAmount }}</strong></span>
                <span class="ml-20">定金: ¥{{ order.depositAmount }}</span>
                <span class="ml-20">尾款: ¥{{ order.finalAmount }}</span>
              </div>
            </div>
            <div class="order-actions mt-10 flex-between">
              <el-button type="primary" size="small" @click="$router.push(`/order/${order.id}`)">查看详情</el-button>
              <div>
                <el-button v-if="order.status === 0" type="primary" size="small" @click="handlePayDeposit(order)">支付定金</el-button>
                <el-button v-if="order.status === 5" type="danger" size="small" @click="handlePayFinal(order)">支付尾款</el-button>
                <el-button v-if="[0, 1].includes(order.status)" type="info" size="small" @click="handleCancel(order)">取消订单</el-button>
              </div>
            </div>
          </el-card>
        </div>

        <div class="flex-center mt-20">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="10"
            :total="total"
            layout="total, prev, pager, next"
            @current-change="fetchOrders"
          />
        </div>
      </el-card>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, cancelOrder, payFinal } from '@/api/order'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const activeTab = ref('all')
const orders = ref([])
const currentPage = ref(1)
const total = ref(0)

const statusMap = {
  0: { text: '待付定金', type: 'info' },
  1: { text: '待开发', type: 'warning' },
  2: { text: '开发中', type: '' },
  3: { text: '已交付', type: 'success' },
  4: { text: '验收中', type: 'warning' },
  5: { text: '待付尾款', type: 'danger' },
  6: { text: '已完成', type: 'success' },
  7: { text: '已取消', type: 'info' }
}

const backendStatusMap = {
  pending: 0,
  paid: 1,
  processing: 2,
  delivered: 3,
  reviewing: 4,
  final_pending: 5,
  completed: 6,
  cancelled: 7
}

function statusTagType(status) {
  const index = typeof status === 'number' ? status : (backendStatusMap[status] ?? 0)
  return statusMap[index]?.type || 'info'
}

function getStatusText(status) {
  return statusMap[backendStatusMap[status] ?? 0]?.text || status || '未知'
}

async function fetchOrders() {
  try {
    const res = await getOrderList()
    orders.value = (res.data || []).map(item => ({
      id: item.id,
      orderNo: item.orderNo || '无订单号',
      status: item.status,
      statusText: statusMap[backendStatusMap[item.status] ?? 0]?.text || item.status || '未知',
      createdAt: item.createTime ? new Date(item.createTime).toLocaleString() : '-',
      serviceTitle: '服务 #' + item.serviceId,
      totalAmount: item.amount || 0,
      depositAmount: Math.floor((item.amount || 0) * 0.5),
      finalAmount: Math.ceil((item.amount || 0) * 0.5)
    }))
    total.value = orders.value.length
  } catch (e) {
    orders.value = []
    total.value = 0
  }
}

function handleTabChange() {
  currentPage.value = 1
  fetchOrders()
}

async function handlePayDeposit(order) {
  try {
    await payFinal(order.id)
    ElMessage.success('支付成功')
    fetchOrders()
  } catch (e) {
    // handled by interceptor
  }
}

async function handlePayFinal(order) {
  try {
    await ElMessageBox.confirm(`确认支付尾款 ¥${order.finalAmount}？`, '支付尾款', { type: 'warning' })
    await payFinal(order.id)
    ElMessage.success('尾款支付成功')
    fetchOrders()
  } catch (e) {
    // handled by interceptor
  }
}

async function handleCancel(order) {
  try {
    await ElMessageBox.confirm('确定取消该订单吗？', '取消订单', { type: 'warning' })
    await cancelOrder(order.id)
    ElMessage.success('订单已取消')
    fetchOrders()
  } catch (e) {
    // handled by interceptor
  }
}

onMounted(fetchOrders)
</script>

<style scoped lang="scss">
.order-card {
  margin-bottom: 16px;
}

.order-no {
  font-weight: bold;
}

.order-amounts {
  color: #606266;
  font-size: 13px;
}

.text-primary {
  color: #409EFF;
}

.text-muted {
  color: #909399;
}

.ml-10 {
  margin-left: 10px;
}

.ml-20 {
  margin-left: 20px;
}
</style>
