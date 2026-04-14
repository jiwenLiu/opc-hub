<template>
  <div class="order-detail-page">
    <AppHeader />
    <div class="container mt-20">
      <el-page-header @back="$router.back()" class="mb-20">
        <template #content>
          <span>订单详情</span>
        </template>
      </el-page-header>

      <el-card class="mb-20">
        <div class="order-header flex-between">
          <div>
            <h3>{{ order.orderNo }}</h3>
            <el-tag :type="statusTagType(order.status)" size="small">{{ order.statusText }}</el-tag>
          </div>
          <div class="text-right">
            <div>创建时间: {{ order.createdAt }}</div>
            <div class="text-muted">服务提供方: {{ order.opcName }}</div>
          </div>
        </div>
      </el-card>

      <el-card class="mb-20">
        <template #header>付款流程（定金 + 尾款）</template>
        <el-steps :active="currentStep" finish-status="success" align-center>
          <el-step title="创建订单" description="订单已创建" />
          <el-step title="支付定金" :description="`¥${order.depositAmount || 0} (30%)`" />
          <el-step title="开发中" description="OPC开发交付" />
          <el-step title="验收确认" description="企业验收" />
          <el-step title="支付尾款" :description="`¥${order.finalAmount || 0} (70%)`" />
          <el-step title="交易完成" description="平台结算" />
        </el-steps>
      </el-card>

      <el-row :gutter="20">
        <el-col :span="16">
          <el-card class="mb-20">
            <template #header>服务信息</template>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="服务项目">{{ order.serviceTitle }}</el-descriptions-item>
              <el-descriptions-item label="服务分类">{{ order.category }}</el-descriptions-item>
              <el-descriptions-item label="交付周期">{{ order.deliveryDays }} 天</el-descriptions-item>
              <el-descriptions-item label="下单时间">{{ order.createdAt }}</el-descriptions-item>
            </el-descriptions>
          </el-card>

          <el-card v-if="order.deliveryContent" class="mb-20">
            <template #header>交付内容</template>
            <div class="delivery-content">{{ order.deliveryContent }}</div>
            <div v-if="order.deliveryFiles && order.deliveryFiles.length" class="mt-10">
              <div v-for="(file, index) in order.deliveryFiles" :key="index">
                <el-link type="primary" :href="file.url" target="_blank">{{ file.name }}</el-link>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="mb-20">
            <template #header>费用明细</template>
            <div class="fee-item">
              <span>服务总金额</span>
              <span class="fee-value">¥{{ order.totalAmount }}</span>
            </div>
            <div class="fee-item">
              <span>定金 (30%)</span>
              <span class="fee-value text-warning">¥{{ order.depositAmount }}</span>
            </div>
            <div class="fee-item">
              <span>尾款 (70%)</span>
              <span class="fee-value text-danger">¥{{ order.finalAmount }}</span>
            </div>
            <div class="fee-item">
              <span>平台服务费 (5%)</span>
              <span class="fee-value text-muted">¥{{ order.platformFee }}</span>
            </div>
            <el-divider />
            <div class="fee-item fee-total">
              <span>已支付</span>
              <span class="fee-value text-primary">¥{{ order.paidAmount }}</span>
            </div>
            <div class="fee-item fee-total" v-if="order.status < 6">
              <span>待支付</span>
              <span class="fee-value text-danger">¥{{ order.unpaidAmount }}</span>
            </div>
          </el-card>

          <el-card>
            <template #header>操作</template>
            <div class="actions">
              <el-button
                v-if="order.status === 0"
                type="primary"
                class="full-width mb-10"
                @click="handlePayDeposit"
              >
                支付定金 ¥{{ order.depositAmount }}
              </el-button>

              <el-button
                v-if="order.status === 2"
                type="success"
                class="full-width mb-10"
                @click="handleDeliver"
              >
                确认交付
              </el-button>

              <el-button
                v-if="order.status === 3"
                type="warning"
                class="full-width mb-10"
                @click="handleAccept"
              >
                验收确认
              </el-button>

              <el-button
                v-if="order.status === 5"
                type="danger"
                class="full-width mb-10"
                @click="handlePayFinal"
              >
                支付尾款 ¥{{ order.finalAmount }}
              </el-button>

              <el-button
                v-if="[0, 1].includes(order.status)"
                class="full-width"
                @click="handleCancel"
              >
                取消订单
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderDetail, acceptOrder, deliverOrder, cancelOrder, payFinal } from '@/api/order'
import { payDeposit } from '@/api/payment'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const route = useRoute()
const order = ref({})

const statusMap = {
  0: { text: '待付定金', type: 'info' },
  1: { text: '已付定金/待开发', type: 'warning' },
  2: { text: '开发中', type: '' },
  3: { text: '已交付', type: 'success' },
  4: { text: '验收中', type: 'warning' },
  5: { text: '待付尾款', type: 'danger' },
  6: { text: '已完成', type: 'success' },
  7: { text: '已取消', type: 'info' }
}

const currentStep = computed(() => {
  const stepMap = { 0: 0, 1: 1, 2: 2, 3: 3, 4: 3, 5: 4, 6: 5, 7: 0 }
  return stepMap[order.value.status] ?? 0
})

function statusTagType(status) {
  return statusMap[status]?.type || 'info'
}

async function fetchOrder() {
  try {
    const res = await getOrderDetail(route.params.id)
    order.value = res.data
  } catch (e) {
    // handled by interceptor
  }
}

async function handlePayDeposit() {
  try {
    await payDeposit({ orderId: order.value.id })
    ElMessage.success('定金支付成功')
    fetchOrder()
  } catch (e) {
    // handled by interceptor
  }
}

async function handleDeliver() {
  try {
    await deliverOrder({ orderId: order.value.id })
    ElMessage.success('已确认交付')
    fetchOrder()
  } catch (e) {
    // handled by interceptor
  }
}

async function handleAccept() {
  try {
    await ElMessageBox.confirm('确认验收通过？', '验收确认', { type: 'warning' })
    await acceptOrder(order.value.id)
    ElMessage.success('验收通过，请支付尾款')
    fetchOrder()
  } catch (e) {
    // handled by interceptor
  }
}

async function handlePayFinal() {
  try {
    await ElMessageBox.confirm(`确认支付尾款 ¥${order.value.finalAmount}？`, '支付尾款', { type: 'warning' })
    await payFinal(order.value.id)
    ElMessage.success('尾款支付成功，交易完成')
    fetchOrder()
  } catch (e) {
    // handled by interceptor
  }
}

async function handleCancel() {
  try {
    await ElMessageBox.confirm('确定取消该订单吗？', '取消订单', { type: 'warning' })
    await cancelOrder(order.value.id)
    ElMessage.success('订单已取消')
    fetchOrder()
  } catch (e) {
    // handled by interceptor
  }
}

onMounted(fetchOrder)
</script>

<style scoped lang="scss">
.fee-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px dashed #ebeef5;

  &:last-child {
    border-bottom: none;
  }
}

.fee-value {
  font-weight: bold;
}

.fee-total {
  font-size: 16px;
}

.text-warning {
  color: #E6A23C;
}

.text-danger {
  color: #F56C6C;
}

.text-primary {
  color: #409EFF;
}

.text-muted {
  color: #909399;
}

.full-width {
  width: 100%;
}

.delivery-content {
  line-height: 1.8;
  color: #606266;
}
</style>
