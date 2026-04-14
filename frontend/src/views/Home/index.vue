<template>
  <div class="home-page">
    <AppHeader />
    <section class="hero">
      <div class="container text-center">
        <h1>OPC Hub 智能服务平台</h1>
        <p class="hero-desc">连接企业与优秀OPC，AI智能匹配，高效交付</p>
        <div class="search-bar">
          <el-input
            v-model="keyword"
            placeholder="搜索服务、工具或OPC..."
            size="large"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
            </template>
          </el-input>
        </div>
      </div>
    </section>

    <section class="categories container mt-20">
      <el-tabs v-model="activeCategory" type="card">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="设计" name="design" />
        <el-tab-pane label="开发" name="dev" />
        <el-tab-pane label="营销" name="marketing" />
        <el-tab-pane label="内容" name="content" />
        <el-tab-pane label="咨询" name="consulting" />
      </el-tabs>
    </section>

    <section class="services container mt-20">
      <div class="flex-between mb-20">
        <h2>热门服务</h2>
        <el-button type="primary" @click="$router.push('/match')">
          <el-icon><MagicStick /></el-icon> 智能匹配
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in services" :key="item.id">
          <ServiceCard :service="item" />
        </el-col>
      </el-row>
    </section>

    <section class="match-promo container mt-20">
      <el-card shadow="hover" class="promo-card">
        <div class="flex-between">
          <div>
            <h2>AI 智能匹配</h2>
            <p>描述您的需求，AI为您精准匹配最合适的OPC</p>
            <el-button type="primary" size="large" class="mt-10" @click="$router.push('/match')">
              立即体验
            </el-button>
          </div>
          <el-icon :size="120" color="#409EFF"><Cpu /></el-icon>
        </div>
      </el-card>
    </section>

    <AppFooter />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'
import ServiceCard from '@/components/ServiceCard/index.vue'

const router = useRouter()
const keyword = ref('')
const activeCategory = ref('all')

const services = ref([
  { id: 1, title: '企业官网设计与开发', cover: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/3b4da2ea1bb84bfeae68593371a21d71.jpg', opcName: '张明', opcLevel: 'L5', price: 15000, rating: 4.9, sales: 128, category: '开发' },
  { id: 2, title: '品牌VI视觉设计', cover: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/977da4bc3ae449aa9290520d60632e76.jpg', opcName: '李华', opcLevel: 'L4', price: 8000, rating: 4.8, sales: 96, category: '设计' },
  { id: 3, title: '小程序全栈开发', cover: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/31dd8c480dc64435974d2c43fc00a857.jpg', opcName: '王强', opcLevel: 'L6', price: 25000, rating: 5.0, sales: 215, category: '开发' },
  { id: 4, title: 'SEO优化与内容营销', cover: 'https://modao.cc/agent-py/media/generated_images/2026-03-30/df3e552d825c450db2822021534fd445.jpg', opcName: '赵敏', opcLevel: 'L3', price: 5000, rating: 4.7, sales: 67, category: '营销' }
])

function handleSearch() {
  router.push(`/tools?q=${keyword.value}`)
}
</script>

<style scoped lang="scss">
.hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 80px 0;
  color: #fff;

  h1 {
    font-size: 42px;
    margin-bottom: 16px;
  }

  .hero-desc {
    font-size: 18px;
    margin-bottom: 30px;
    opacity: 0.9;
  }

  .search-bar {
    max-width: 600px;
    margin: 0 auto;
  }
}

.promo-card {
  padding: 20px;
}

.services h2 {
  font-size: 24px;
}
</style>
