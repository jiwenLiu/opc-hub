<template>
  <div class="requirement-list-page">
    <AppHeader />
    <div class="container mt-20">
      <div class="flex-between mb-20">
        <h2>我的需求</h2>
        <el-button type="primary" @click="$router.push('/enterprise/requirement/publish')">
          <el-icon><Plus /></el-icon> 发布新需求
        </el-button>
      </div>

      <el-card>
        <el-table :data="requirements" stripe>
          <el-table-column prop="title" label="需求标题" min-width="200" />
          <el-table-column prop="category" label="分类" width="100">
            <template #default="{ row }">
              {{ categoryMap[row.category] || row.category }}
            </template>
          </el-table-column>
          <el-table-column prop="budget" label="预算" width="120">
            <template #default="{ row }">¥{{ row.budget }}</template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="{ row }">
              <el-tag :type="statusTagType(row.status)" size="small">{{ row.statusText }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="发布时间" width="180" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button type="primary" link size="small">查看</el-button>
              <el-button type="danger" link size="small">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="flex-center mt-20">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="10"
            :total="total"
            layout="total, prev, pager, next"
          />
        </div>
      </el-card>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRequirementList } from '@/api/match'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const categoryMap = { design: '设计', dev: '开发', marketing: '营销', content: '内容', consulting: '咨询' }
const requirements = ref([])
const currentPage = ref(1)
const total = ref(0)

const statusMap = {
  0: { text: '待匹配', type: 'info' },
  1: { text: '匹配中', type: 'warning' },
  2: { text: '已匹配', type: 'success' },
  3: { text: '已关闭', type: 'info' }
}

function statusTagType(status) {
  return statusMap[status]?.type || 'info'
}

async function fetchRequirements() {
  try {
    const res = await getRequirementList({ page: currentPage.value, pageSize: 10 })
    requirements.value = (res.data.list || []).map(item => ({
      ...item,
      statusText: statusMap[item.status]?.text || '未知'
    }))
    total.value = res.data.total || 0
  } catch (e) {
    // handled by interceptor
  }
}

onMounted(fetchRequirements)
</script>
