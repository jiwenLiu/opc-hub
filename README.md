# OPC Hub 单机版启动说明

## 环境要求

1. Java 17+
2. MySQL 8.0+
3. Redis 7.0+
4. Node.js 18+

## 快速启动

### 1. 数据库初始化

```bash
# 登录MySQL并执行初始化脚本
mysql -u root -p < db/schema.sql
```

### 2. 后端启动

方式一：直接运行jar
```bash
cd opc-hub
mvn clean package -DskipTests
java -jar target/opc-hub-1.0.0.jar
```

方式二：IDE启动
```
直接运行 OpcHubApplication.java
```

### 3. 前端启动

```bash
cd frontend
npm install
npm run dev
```

## 配置说明

后端配置文件：`opc-hub/src/main/resources/application.yml`

主要配置项：
- `spring.datasource` - 数据库连接
- `spring.data.redis` - Redis连接
- `server.port` - 服务端口（默认8080）

## 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| testopc | 123456 | 一人公司 |
| testent | 123456 | 企业 |

## 接口文档

启动后访问：http://localhost:8080/doc.html (Knife4j文档)