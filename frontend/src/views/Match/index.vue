<template>
  <div class="match-page">
    <AppHeader />
    <div class="container mt-20">
      <h2 class="mb-20">AI 智能匹配</h2>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card>
            <template #header>需求描述</template>
            <el-form :model="form" label-position="top" @submit.prevent="handleMatch">
              <el-form-item label="需求描述">
                <el-input v-model="form.description" type="textarea" :rows="5" placeholder="请详细描述您的需求..." />
              </el-form-item>
              <el-form-item label="需求分类">
                <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
                  <el-option label="设计" value="design" />
                  <el-option label="开发" value="dev" />
                  <el-option label="营销" value="marketing" />
                  <el-option label="内容" value="content" />
                  <el-option label="咨询" value="consulting" />
                </el-select>
              </el-form-item>
              <el-form-item label="预算（元）">
                <el-input-number v-model="form.budget" :min="0" :step="1000" style="width: 100%" />
              </el-form-item>
              <el-form-item label="期望交付（天）">
                <el-input-number v-model="form.deliveryDays" :min="1" :max="365" style="width: 100%" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :loading="loading" class="full-width" @click="handleMatch">
                  <el-icon><MagicStick /></el-icon> 开始匹配
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>

        <el-col :span="16">
          <el-card>
            <template #header>
              <div class="flex-between">
                <span>匹配结果</span>
                <span v-if="matchResults.length">共 {{ matchResults.length }} 条匹配</span>
              </div>
            </template>

            <div v-if="loading" class="text-center" style="padding: 40px">
              <el-icon class="is-loading" :size="40"><Loading /></el-icon>
              <p class="mt-10">AI 正在为您匹配...</p>
            </div>

            <el-empty v-else-if="!matchResults.length" description="请输入需求后开始匹配" />

            <div v-else>
              <el-card v-for="item in matchResults" :key="item.id" shadow="hover" class="match-card">
                <div class="flex-between">
                  <div>
                    <div class="opc-info">
                      <el-avatar :size="48" :src="item.avatar">{{ item.opcName[0] }}</el-avatar>
                      <div class="ml-10">
                        <div class="opc-name">{{ item.opcName }} <el-tag size="small" type="warning">L{{ item.level }}</el-tag></div>
                        <div class="opc-meta">
                          <el-rate v-model="item.rating" disabled :colors="['#F7BA2A', '#F7BA2A', '#FF9900']" size="small" />
                          <span class="ml-10">好评率 {{ item.goodRate }}%</span>
                        </div>
                      </div>
                    </div>
                    <div class="mt-10">
                      <el-tag v-for="tag in item.tags" :key="tag" size="small" class="mr-5">{{ tag }}</el-tag>
                    </div>
                    <div class="mt-10 text-muted">{{ item.description }}</div>
                  </div>
                  <div class="text-right">
                    <div class="match-percent">{{ item.matchPercent }}% 匹配</div>
                    <div class="price">¥{{ item.price }}</div>
                    <div class="delivery">{{ item.deliveryDays }}天交付</div>
                    <div class="mt-10">
                      <el-button type="primary" size="small" @click="handleAccept(item)">接受</el-button>
                      <el-button size="small" @click="handleReject(item)">拒绝</el-button>
                    </div>
                  </div>
                </div>
              </el-card>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { aiMatch } from '@/api/match'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const router = useRouter()
const loading = ref(false)
const matchResults = ref([])

const form = reactive({
  description: '',
  category: '',
  budget: 0,
  deliveryDays: 30
})

async function handleMatch() {
  if (!form.description) {
    ElMessage.warning('请输入需求描述')
    return
  }
  loading.value = true
  try {
    const res = await aiMatch(form)
    matchResults.value = res.data || []
    if (matchResults.value.length === 0) {
      matchResults.value = [
        { id: 1, opcName: '张明', level: 5, rating: 4.9, goodRate: 98, tags: ['前端', 'Vue', 'React'], description: '8年经验，擅长企业级应用开发', avatar: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/65d229a90650405b87e4276884204fc4.jpg', matchPercent: 95, price: 15000, deliveryDays: 7 },
        { id: 2, opcName: '李华', level: 4, rating: 4.8, goodRate: 96, tags: ['UI设计', '品牌VI'], description: '资深设计师，专注品牌视觉设计', avatar: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/76aef5ed927d471392122f927bda825f.jpg', matchPercent: 88, price: 8000, deliveryDays: 5 },
        { id: 3, opcName: '王强', level: 6, rating: 5.0, goodRate: 99, tags: ['全栈', '小程序', '后端'], description: '技术大牛，擅长复杂系统架构', avatar: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/ac916c0a26c24c529e008302f8e8f08a.jpg', matchPercent: 82, price: 25000, deliveryDays: 14 }
      ]
    }
  } catch (e) {
    matchResults.value = [
      { id: 1, opcName: '张明', level: 5, rating: 4.9, goodRate: 98, tags: ['前端', 'Vue', 'React'], description: '8年经验，擅长企业级应用开发', avatar: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/65d229a90650405b87e4276884204fc4.jpg', matchPercent: 95, price: 15000, deliveryDays: 7 },
      { id: 2, opcName: '李华', level: 4, rating: 4.8, goodRate: 96, tags: ['UI设计', '品牌VI'], description: '资深设计师，专注品牌视觉设计', avatar: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/76aef5ed927d471392122f927bda825f.jpg', matchPercent: 88, price: 8000, deliveryDays: 5 },
      { id: 3, opcName: '王强', level: 6, rating: 5.0, goodRate: 99, tags: ['全栈', '小程序', '后端'], description: '技术大牛，擅长复杂系统架构', avatar: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/ac916c0a26c24c529e008302f8e8f08a.jpg', matchPercent: 82, price: 25000, deliveryDays: 14 }
    ]
  } finally {
    loading.value = false
  }
}

function handleAccept(item) {
  ElMessage.success(`已接受 ${item.opcName} 的方案`)
  router.push({ path: '/orders', query: { opcId: item.opcId, price: item.price } })
}

function handleReject(item) {
  matchResults.value = matchResults.value.filter(r => r.id !== item.id)
  ElMessage.info('已拒绝该方案')
}
</script>

<style scoped lang="scss">
.full-width {
  width: 100%;
}

.match-card {
  margin-bottom: 16px;
}

.opc-info {
  display: flex;
  align-items: center;
}

.opc-name {
  font-weight: bold;
  font-size: 16px;
}

.opc-meta {
  display: flex;
  align-items: center;
  margin-top: 4px;
  font-size: 12px;
  color: #909399;
}

.match-percent {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.price {
  font-size: 20px;
  color: #F56C6C;
  font-weight: bold;
  margin-top: 4px;
}

.delivery {
  color: #909399;
  margin-top: 4px;
}

.mr-5 {
  margin-right: 5px;
}

.ml-10 {
  margin-left: 10px;
}

.text-muted {
  color: #909399;
}
</style>
