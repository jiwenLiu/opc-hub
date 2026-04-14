<template>
  <div class="service-publish-page">
    <AppHeader />
    <div class="container mt-20">
      <h2 class="mb-20">发布服务</h2>
      <el-card>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
          @submit.prevent="handleSubmit"
        >
          <el-form-item label="服务标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入服务标题" maxlength="100" show-word-limit />
          </el-form-item>

          <el-form-item label="服务分类" prop="category">
            <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
              <el-option label="设计" value="design" />
              <el-option label="开发" value="dev" />
              <el-option label="营销" value="marketing" />
              <el-option label="内容" value="content" />
              <el-option label="咨询" value="consulting" />
            </el-select>
          </el-form-item>

          <el-form-item label="服务价格" prop="price">
            <el-input-number v-model="form.price" :min="0" :precision="2" :step="100" />
            <span class="ml-10">元</span>
          </el-form-item>

          <el-form-item label="交付周期" prop="deliveryDays">
            <el-input-number v-model="form.deliveryDays" :min="1" :max="365" />
            <span class="ml-10">天</span>
          </el-form-item>

          <el-form-item label="服务描述" prop="description">
            <el-input v-model="form.description" type="textarea" :rows="6" placeholder="请详细描述您的服务内容、流程、优势等" />
          </el-form-item>

          <el-form-item label="服务封面">
            <el-upload
              action="/api/upload"
              :limit="1"
              list-type="picture-card"
              :on-success="handleUploadSuccess"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <el-form-item label="包含内容">
            <div v-for="(item, index) in form.includeItems" :key="index" class="mb-10">
              <el-input v-model="form.includeItems[index]" placeholder="请输入包含的服务项" style="width: 80%">
                <template #append>
                  <el-button @click="form.includeItems.splice(index, 1)">删除</el-button>
                </template>
              </el-input>
            </div>
            <el-button @click="form.includeItems.push('')">+ 添加包含项</el-button>
          </el-form-item>

          <el-form-item label="不包含内容">
            <div v-for="(item, index) in form.excludeItems" :key="index" class="mb-10">
              <el-input v-model="form.excludeItems[index]" placeholder="请输入不包含的服务项" style="width: 80%">
                <template #append>
                  <el-button @click="form.excludeItems.splice(index, 1)">删除</el-button>
                </template>
              </el-input>
            </div>
            <el-button @click="form.excludeItems.push('')">+ 添加排除项</el-button>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleSubmit">发布服务</el-button>
            <el-button @click="$router.back()">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { submitOpcService } from '@/api/user'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  title: '',
  category: '',
  price: 0,
  deliveryDays: 7,
  description: '',
  coverUrl: '',
  includeItems: [''],
  excludeItems: ['']
})

const rules = {
  title: [{ required: true, message: '请输入服务标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择服务分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入服务价格', trigger: 'blur' }],
  deliveryDays: [{ required: true, message: '请输入交付周期', trigger: 'blur' }],
  description: [{ required: true, message: '请输入服务描述', trigger: 'blur' }]
}

function handleUploadSuccess(res) {
  form.coverUrl = res.data.url
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await submitOpcService(form)
    ElMessage.success('服务发布成功')
    router.push('/opc/services')
  } catch (e) {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.ml-10 {
  margin-left: 10px;
}
</style>
