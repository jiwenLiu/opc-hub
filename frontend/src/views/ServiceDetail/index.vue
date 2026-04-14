<template>
  <div class="service-detail-page">
    <AppHeader />
    <div class="container mt-20">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card class="mb-20">
            <el-carousel height="400px" v-if="service.images && service.images.length">
              <el-carousel-item v-for="(img, index) in service.images" :key="index">
                <el-image :src="img" fit="contain" style="width: 100%; height: 100%" />
              </el-carousel-item>
            </el-carousel>
            <el-empty v-else description="暂无图片" />
          </el-card>

          <el-card class="mb-20">
            <template #header>服务详情</template>
            <div class="service-desc">{{ service.description }}</div>
          </el-card>

          <el-card v-if="service.includeItems && service.includeItems.length" class="mb-20">
            <template #header>包含内容</template>
            <ul class="list-check">
              <li v-for="(item, index) in service.includeItems" :key="index">
                <el-icon color="#67C23A"><CircleCheck /></el-icon> {{ item }}
              </li>
            </ul>
          </el-card>

          <el-card v-if="service.excludeItems && service.excludeItems.length">
            <template #header>不包含内容</template>
            <ul class="list-cross">
              <li v-for="(item, index) in service.excludeItems" :key="index">
                <el-icon color="#F56C6C"><CircleClose /></el-icon> {{ item }}
              </li>
            </ul>
          </el-card>
        </el-col>

        <el-col :span="8">
          <div class="sticky-panel">
            <el-card class="mb-20">
              <div class="service-price">
                <span class="price-label">价格</span>
                <span class="price-value">¥{{ service.price }}</span>
              </div>
              <el-descriptions :column="1" class="mt-10">
                <el-descriptions-item label="交付周期">{{ service.deliveryDays }} 天</el-descriptions-item>
                <el-descriptions-item label="销量">{{ service.sales }} 单</el-descriptions-item>
                <el-descriptions-item label="评分">
                  <el-rate v-model="service.rating" disabled />
                </el-descriptions-item>
              </el-descriptions>
              <el-button type="primary" class="full-width mt-20" size="large" @click="handlePurchase">
                立即购买
              </el-button>
              <el-button class="full-width mt-10" @click="handleContact">联系OPC</el-button>
            </el-card>

            <el-card>
              <template #header>服务提供方</template>
              <div class="opc-profile">
                <el-avatar :size="60">{{ service.opcName?.[0] || 'O' }}</el-avatar>
                <div class="ml-10">
                  <div class="opc-name">{{ service.opcName }}</div>
                  <el-tag size="small" type="warning">L{{ service.opcLevel }}</el-tag>
                  <div class="opc-stats mt-10">
                    <span>完成: {{ service.completedCount }}</span>
                    <span class="ml-10">好评: {{ service.goodRate }}%</span>
                  </div>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>

      <el-card class="mt-20">
        <template #header>用户评价</template>
        <div v-if="reviews.length === 0" class="text-center" style="padding: 20px">
          <el-empty description="暂无评价" />
        </div>
        <div v-else>
          <div v-for="review in reviews" :key="review.id" class="review-item">
            <div class="flex-between">
              <div class="flex-center">
                <el-avatar :size="36">{{ review.userName[0] }}</el-avatar>
                <span class="ml-10">{{ review.userName }}</span>
              </div>
              <el-rate v-model="review.rating" disabled size="small" />
            </div>
            <div class="review-content mt-10">{{ review.content }}</div>
            <div class="text-muted mt-10">{{ review.createdAt }}</div>
          </div>
        </div>
      </el-card>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOpcServiceDetail } from '@/api/user'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const route = useRoute()
const router = useRouter()
const service = ref({})
const reviews = ref([])

async function fetchService() {
  try {
    const res = await getOpcServiceDetail(route.params.id)
    service.value = res.data
    reviews.value = res.data.reviews || []
    if (!service.value.images || service.value.images.length === 0) {
      service.value.images = [
        'https://modao.cc/agent-py/media/generated_images/2026-03-30/d864480b3d994656b5ada18e9282ec82.jpg',
        'https://modao.cc/agent-py/media/generated_images/2026-03-30/ac916c0a26c24c529e008302f8e8f08a.jpg',
        'https://modao.cc/agent-py/media/generated_images/2026-03-30/76aef5ed927d471392122f927bda825f.jpg'
      ]
    }
  } catch (e) {
    service.value = {
      images: [
        'https://modao.cc/agent-py/media/generated_images/2026-03-30/d864480b3d994656b5ada18e9282ec82.jpg',
        'https://modao.cc/agent-py/media/generated_images/2026-03-30/ac916c0a26c24c529e008302f8e8f08a.jpg',
        'https://modao.cc/agent-py/media/generated_images/2026-03-30/76aef5ed927d471392122f927bda825f.jpg'
      ]
    }
  }
}

function handlePurchase() {
  router.push({ path: '/orders', query: { serviceId: service.value.id } })
}

function handleContact() {
  router.push('/message')
}

onMounted(fetchService)
</script>

<style scoped lang="scss">
.sticky-panel {
  position: sticky;
  top: 20px;
}

.service-price {
  display: flex;
  align-items: baseline;
}

.price-label {
  color: #909399;
  margin-right: 10px;
}

.price-value {
  font-size: 32px;
  font-weight: bold;
  color: #F56C6C;
}

.full-width {
  width: 100%;
}

.opc-profile {
  display: flex;
  align-items: flex-start;
}

.opc-name {
  font-weight: bold;
}

.opc-stats {
  font-size: 12px;
  color: #909399;
}

.list-check, .list-cross {
  li {
    display: flex;
    align-items: center;
    padding: 6px 0;
    gap: 8px;
  }
}

.review-item {
  padding: 16px 0;
  border-bottom: 1px solid #ebeef5;

  &:last-child {
    border-bottom: none;
  }
}

.review-content {
  color: #606266;
  line-height: 1.6;
}

.text-muted {
  color: #909399;
  font-size: 12px;
}

.ml-10 {
  margin-left: 10px;
}
</style>
