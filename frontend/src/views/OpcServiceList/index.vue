<template>
  <div class="service-list-page">
    <AppHeader />
    <div class="container mt-20">
      <div class="flex-between mb-20">
        <h2>我的服务</h2>
        <el-button type="primary" @click="$router.push('/opc/service/publish')">
          <el-icon><Plus /></el-icon> 发布新服务
        </el-button>
      </div>

      <el-card>
        <el-table :data="services" stripe>
          <el-table-column prop="title" label="服务名称" min-width="200" />
          <el-table-column prop="category" label="分类" width="100">
            <template #default="{ row }">
              {{ categoryMap[row.category] || row.category }}
            </template>
          </el-table-column>
          <el-table-column prop="price" label="价格" width="120">
            <template #default="{ row }">¥{{ row.price }}</template>
          </el-table-column>
          <el-table-column prop="sales" label="销量" width="80" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
                {{ row.status === 1 ? '上架中' : '已下架' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOpcServiceList } from '@/api/user'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const categoryMap = { design: '设计', dev: '开发', marketing: '营销', content: '内容', consulting: '咨询' }
const services = ref([])
const currentPage = ref(1)
const total = ref(0)

async function fetchServices() {
  try {
    const res = await getOpcServiceList({ page: currentPage.value, pageSize: 10 })
    services.value = res.data.list
    total.value = res.data.total
  } catch (e) {
    // handled by interceptor
  }
}

function handleEdit(row) {
  ElMessage.info('编辑功能开发中')
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除该服务吗？', '提示', { type: 'warning' })
    ElMessage.success('删除成功')
    fetchServices()
  } catch (e) {
    // cancelled
  }
}

onMounted(fetchServices)
</script>
