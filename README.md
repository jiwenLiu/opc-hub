# OPC Hub 智能服务平台

## 项目介绍

OPC Hub 是一个连接企业与优秀OPC（一人公司）的智能服务平台，提供AI智能匹配、需求发布、订单管理等功能。

## 技术栈

### 后端
- Java 17
- Spring Boot 3.2.5
- MyBatis Plus
- MySQL 8.0
- Redis
- Sa-Token (权限认证)
- Redisson (分布式锁)

### 前端
- Vue 3
- Vite
- Element Plus
- Pinia (状态管理)
- Vue Router

## 项目结构

```
haitao_beijing_single/
├── opc-hub/                 # 后端Java项目
│   ├── src/main/java/
│   │   └── com/opchub/
│   │       ├── controller/  # 控制器
│   │       ├── entity/      # 实体类
│   │       ├── mapper/      # 数据访问层
│   │       ├── service/     # 业务逻辑
│   │       ├── config/      # 配置类
│   │       ├── common/      # 公共组件
│   │       └── vo/          # 视图对象
│   └── src/main/resources/
│       ├── application.yml  # 应用配置
│       └── redisson.yml     # Redis配置
│
├── frontend/                # 前端Vue项目
│   ├── src/
│   │   ├── api/            # API接口
│   │   ├── components/     # 公共组件
│   │   ├── views/          # 页面视图
│   │   ├── stores/         # Pinia状态管理
│   │   ├── router/         # 路由配置
│   │   └── styles/         # 样式文件
│   └── vite.config.js      # Vite配置
│
└── db/
    └── schema.sql          # 数据库脚本
```

## 功能模块

### 用户角色
- **OPC（一人公司）**: 提供服务、接受订单
- **企业用户**: 发布需求、下单购买服务

### 核心功能
1. **用户认证**: 登录、注册、权限管理
2. **服务管理**: OPC发布服务、查看服务列表
3. **需求管理**: 企业发布需求、查看需求列表
4. **智能匹配**: AI推荐最合适的OPC
5. **订单管理**: 创建订单、支付、交付、验收
6. **企业/OPC认证**: 实名认证体系
7. **消息中心**: 站内消息沟通
8. **工具市场**: OPC工具模板交易

## 数据库表

| 表名 | 说明 |
|------|------|
| user | 用户表 |
| opc_service | OPC服务表 |
| enterprise_requirement | 企业需求表 |
| trade_order | 交易订单表 |
| service_category | 服务分类表 |
| enterprise_certification | 企业认证表 |
| opc_certification | OPC认证表 |

## 配置说明

### 数据库配置 (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/opc_hub
    username: root
    password: root
  data:
    redis:
      host: 127.0.0.1
      port: 6379
```

### 测试账号
- OPC用户: testopc / 123456
- 企业用户: testent / 123456

## 运行说明

### 后端启动
```bash
cd opc-hub
mvn spring-boot:run
```
访问: http://localhost:8080/doc.html

### 前端启动
```bash
cd frontend
npm install
npm run dev
```
访问: http://localhost:3000

## API文档

启动后端后访问: http://localhost:8080/doc.html

## 上传记录

- 2026-04-14: 初始版本上传