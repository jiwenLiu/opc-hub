<template>
  <footer class="site-footer">
    <div class="container">
      <el-row :gutter="40">
        <el-col :span="6">
          <h4>OPC Hub</h4>
          <p class="text-muted mt-10">智能OPC服务平台，连接企业与优秀人才</p>
        </el-col>
        <el-col :span="6">
          <h4>服务</h4>
          <ul class="footer-links mt-10">
            <li><router-link to="/tools">工具市场</router-link></li>
            <li><router-link to="/match">智能匹配</router-link></li>
            <li><router-link to="/orders">订单管理</router-link></li>
          </ul>
        </el-col>
        <el-col :span="6">
          <h4>帮助</h4>
          <ul class="footer-links mt-10">
            <li><a href="#">使用指南</a></li>
            <li><a href="#">常见问题</a></li>
            <li><a href="#">联系客服</a></li>
          </ul>
        </el-col>
        <el-col :span="6">
          <h4>联系我们</h4>
          <ul class="footer-links mt-10">
            <li>电话: 400-888-8888</li>
            <li>邮箱: support@opchub.com</li>
            <li>工作时间: 周一至周五 9:00-18:00</li>
          </ul>
        </el-col>
      </el-row>
      <el-divider />
      <div class="text-center text-muted">
        © 2024 OPC Hub. All rights reserved.
      </div>
    </div>
  </footer>
</template>

<style scoped lang="scss">
.site-footer {
  background: #2c3e50;
  color: #fff;
  padding: 40px 0 20px;
  margin-top: 40px;
}

h4 {
  font-size: 16px;
  margin-bottom: 8px;
}

.footer-links {
  li {
    padding: 4px 0;
    color: #bdc3c7;

    a {
      color: #bdc3c7;

      &:hover {
        color: #fff;
      }
    }
  }
}

.text-muted {
  color: #bdc3c7;
}
</style>
