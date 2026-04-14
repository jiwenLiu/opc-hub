<template>
  <el-card shadow="hover" class="service-card" @click="$router.push(`/service/${service.id}`)">
    <div class="card-cover">
      <el-image v-if="service.cover" :src="service.cover" fit="cover" style="width: 100%; height: 160px" />
      <div v-else class="cover-placeholder">
        <el-icon :size="48" color="#c0c4cc"><Picture /></el-icon>
      </div>
      <el-tag class="cover-tag" size="small" type="warning">{{ service.category }}</el-tag>
    </div>
    <div class="card-body">
      <h4 class="card-title">{{ service.title }}</h4>
      <div class="opc-info mt-10">
        <el-avatar :size="24">{{ service.opcName?.[0] || 'O' }}</el-avatar>
        <span class="ml-5">{{ service.opcName }}</span>
        <el-tag size="small" type="warning" class="ml-5">L{{ service.opcLevel }}</el-tag>
      </div>
      <div class="card-meta mt-10 flex-between">
        <div class="rating">
          <el-rate v-model="service.rating" disabled :colors="['#F7BA2A', '#F7BA2A', '#FF9900']" size="small" />
          <span class="ml-5 text-muted">{{ service.rating }}</span>
        </div>
        <span class="text-muted">已售 {{ service.sales }}</span>
      </div>
      <div class="card-footer mt-10 flex-between">
        <span class="price">¥{{ service.price }}</span>
        <el-button type="primary" size="small" @click.stop="$router.push(`/service/${service.id}`)">
          查看详情
        </el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
defineProps({
  service: {
    type: Object,
    required: true,
    default: () => ({})
  }
})
</script>

<style scoped lang="scss">
.service-card {
  cursor: pointer;
  margin-bottom: 20px;
  transition: transform 0.2s;

  &:hover {
    transform: translateY(-4px);
  }
}

.card-cover {
  position: relative;
  height: 160px;
  margin: -20px -20px 16px;
  border-radius: 4px 4px 0 0;
  overflow: hidden;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-tag {
  position: absolute;
  top: 10px;
  right: 10px;
}

.card-title {
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 8px;
}

.opc-info {
  display: flex;
  align-items: center;
  font-size: 12px;
}

.card-meta {
  font-size: 12px;
}

.card-footer {
  border-top: 1px solid #ebeef5;
  padding-top: 10px;
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: #F56C6C;
}

.text-muted {
  color: #909399;
  font-size: 12px;
}

.ml-5 {
  margin-left: 5px;
}
</style>
