<template>
  <div class="tools-page">
    <AppHeader />
    <div class="container mt-20">
      <h2 class="mb-20">OPC 工具市场</h2>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="mb-20">
            <template #header>等级筛选</template>
            <el-checkbox-group v-model="levelFilter">
              <el-checkbox v-for="i in 6" :key="i" :label="i">L{{ i }}</el-checkbox>
            </el-checkbox-group>
          </el-card>
          <el-card>
            <template #header>分类</template>
            <el-menu :default-active="activeCategory" @select="handleCategorySelect">
              <el-menu-item index="all">全部分类</el-menu-item>
              <el-menu-item index="design">设计</el-menu-item>
              <el-menu-item index="dev">开发</el-menu-item>
              <el-menu-item index="marketing">营销</el-menu-item>
              <el-menu-item index="content">内容</el-menu-item>
              <el-menu-item index="consulting">咨询</el-menu-item>
            </el-menu>
          </el-card>
        </el-col>

        <el-col :span="18">
          <el-row :gutter="16">
            <el-col :span="8" v-for="tool in tools" :key="tool.id" class="mb-20">
              <el-card shadow="hover" class="tool-card">
                <div class="tool-cover">
                  <el-image :src="tool.cover" fit="cover" style="width: 100%; height: 140px" />
                </div>
                <div class="tool-info mt-10">
                  <h4 class="tool-title">{{ tool.title }}</h4>
                  <p class="tool-desc text-muted">{{ tool.description }}</p>
                  <div class="flex-between mt-10">
                    <el-tag size="small" type="warning">L{{ tool.level }}</el-tag>
                    <span class="tool-price text-danger">¥{{ tool.price }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <div class="flex-center mt-20">
            <el-pagination
              v-model:current-page="currentPage"
              :page-size="9"
              :total="total"
              layout="total, prev, pager, next"
            />
          </div>
        </el-col>
      </el-row>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const levelFilter = ref([])
const activeCategory = ref('all')
const currentPage = ref(1)
const total = ref(36)

const tools = ref([
  { id: 1, title: '企业官网模板', description: '响应式企业官网模板', level: 3, price: 299, cover: 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=400&h=300&fit=crop' },
  { id: 2, title: '电商小程序', description: '完整电商小程序解决方案', level: 5, price: 999, cover: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400&h=300&fit=crop' },
  { id: 3, title: '数据可视化组件', description: 'ECharts封装组件库', level: 4, price: 199, cover: 'https://images.unsplash.com/photo-1542744094-3a31f272c490?w=400&h=300&fit=crop' },
  { id: 4, title: 'CRM系统模板', description: '客户管理系统前端模板', level: 6, price: 599, cover: 'https://images.unsplash.com/photo-1559028012-481c04fa702d?w=400&h=300&fit=crop' },
  { id: 5, title: '营销H5模板', description: '节日营销H5页面模板', level: 2, price: 99, cover: 'https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=400&h=300&fit=crop' },
  { id: 6, title: '后台管理系统', description: 'Vue3+ElementPlus后台模板', level: 4, price: 399, cover: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400&h=300&fit=crop' },
  { id: 7, title: '博客系统', description: '全栈博客系统模板', level: 5, price: 499, cover: 'https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=400&h=300&fit=crop' },
  { id: 8, title: '在线教育平台', description: '在线教育平台前端模板', level: 5, price: 799, cover: 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=400&h=300&fit=crop' },
  { id: 9, title: '社区论坛', description: '社区论坛前端模板', level: 4, price: 399, cover: 'https://images.unsplash.com/photo-1542744094-3a31f272c490?w=400&h=300&fit=crop' }
])

function handleCategorySelect(key) {
  activeCategory.value = key
}
</script>

<style scoped lang="scss">
.tool-card {
  .tool-title {
    font-size: 14px;
    margin-bottom: 4px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .tool-desc {
    font-size: 12px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }

  .tool-price {
    font-weight: bold;
  }
}

.text-muted {
  color: #909399;
}

.text-danger {
  color: #F56C6C;
}
</style>
