<template>
  <div class="requirement-publish-page">
    <AppHeader />
    <div class="container mt-20">
      <h2 class="mb-20">发布需求</h2>
      <el-card>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
          @submit.prevent="handleSubmit"
        >
          <el-form-item label="需求描述" prop="description">
            <el-input v-model="form.description" type="textarea" :rows="6" placeholder="请详细描述您的需求" />
          </el-form-item>

          <el-form-item label="需求分类" prop="category">
            <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
              <el-option label="设计" value="design" />
              <el-option label="开发" value="dev" />
              <el-option label="营销" value="marketing" />
              <el-option label="内容" value="content" />
              <el-option label="咨询" value="consulting" />
            </el-select>
          </el-form-item>

          <el-form-item label="预算范围" prop="budget">
            <el-input-number v-model="form.budget" :min="0" :precision="2" :step="1000" />
            <span class="ml-10">元</span>
          </el-form-item>

          <el-form-item label="期望交付" prop="deliveryDays">
            <el-input-number v-model="form.deliveryDays" :min="1" :max="365" />
            <span class="ml-10">天</span>
          </el-form-item>

          <el-form-item label="附件">
            <el-upload
              action="/api/upload"
              :limit="5"
              :on-success="handleUploadSuccess"
            >
              <el-button type="primary">上传附件</el-button>
              <template #tip>
                <div class="tip">支持 jpg/png/pdf/doc 格式，最多5个文件</div>
              </template>
            </el-upload>
          </el-form-item>

          <el-form-item label="联系人" prop="contactName">
            <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
          </el-form-item>

          <el-form-item label="联系电话" prop="contactPhone">
            <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleSubmit">发布需求</el-button>
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
import { publishRequirement } from '@/api/match'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  description: '',
  category: '',
  budget: 0,
  deliveryDays: 30,
  attachments: [],
  contactName: '',
  contactPhone: ''
})

const rules = {
  description: [{ required: true, message: '请输入需求描述', trigger: 'blur' }],
  category: [{ required: true, message: '请选择需求分类', trigger: 'change' }],
  budget: [{ required: true, message: '请输入预算', trigger: 'blur' }],
  deliveryDays: [{ required: true, message: '请输入期望交付天数', trigger: 'blur' }],
  contactName: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

function handleUploadSuccess(res) {
  form.attachments.push(res.data.url)
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await publishRequirement(form)
    ElMessage.success('需求发布成功')
    router.push('/enterprise/requirements')
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

.tip {
  color: #909399;
  font-size: 12px;
  margin-top: 4px;
}
</style>
