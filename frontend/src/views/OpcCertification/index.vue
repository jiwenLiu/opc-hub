<template>
  <div class="certification-page">
    <AppHeader />
    <div class="container mt-20">
      <h2 class="mb-20">OPC 实名认证</h2>
      <el-card>
        <el-alert
          v-if="certStatus === 1"
          title="您已通过实名认证"
          type="success"
          :closable="false"
          class="mb-20"
        />
        <el-alert
          v-else-if="certStatus === 2"
          title="认证审核中，请耐心等待"
          type="warning"
          :closable="false"
          class="mb-20"
        />

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
          :disabled="certStatus === 1"
          @submit.prevent="handleSubmit"
        >
          <el-form-item label="真实姓名" prop="realName">
            <el-input v-model="form.realName" placeholder="请输入真实姓名" />
          </el-form-item>

          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="form.idCard" placeholder="请输入身份证号码" />
          </el-form-item>

          <el-form-item label="身份证正面" prop="idCardFront">
            <el-upload
              action="/api/upload"
              :limit="1"
              list-type="picture-card"
              :on-success="(res) => form.idCardFront = res.data.url"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <el-form-item label="身份证反面" prop="idCardBack">
            <el-upload
              action="/api/upload"
              :limit="1"
              list-type="picture-card"
              :on-success="(res) => form.idCardBack = res.data.url"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <el-form-item label="营业执照" prop="businessLicense">
            <el-upload
              action="/api/upload"
              :limit="1"
              list-type="picture-card"
              :on-success="(res) => form.businessLicense = res.data.url"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
            <div class="tip">企业用户请上传营业执照，个人用户可跳过</div>
          </el-form-item>

          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入联系电话" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :loading="loading" @click="handleSubmit">提交认证</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { submitOpcCertification, getOpcCertificationStatus } from '@/api/user'
import AppHeader from '@/components/Header/index.vue'
import AppFooter from '@/components/Footer/index.vue'

const formRef = ref()
const loading = ref(false)
const certStatus = ref(0)

const form = reactive({
  realName: '',
  idCard: '',
  idCardFront: '',
  idCardBack: '',
  businessLicense: '',
  phone: ''
})

const rules = {
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }]
}

async function fetchStatus() {
  try {
    const res = await getOpcCertificationStatus()
    certStatus.value = res.data.status
  } catch (e) {
    // handled by interceptor
  }
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await submitOpcCertification(form)
    ElMessage.success('认证提交成功，等待审核')
    certStatus.value = 2
  } catch (e) {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

onMounted(fetchStatus)
</script>

<style scoped lang="scss">
.tip {
  color: #909399;
  font-size: 12px;
  margin-top: 4px;
}
</style>
