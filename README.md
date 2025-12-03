## warning

本项目只是用来开发学习的。

# 抖音电商后端

## 项目结构

```
douyin_e-commerce_backend/
├── authentication-and-authorization/  # 网关认证授权服务
├── user-service/                      # 用户服务
├── merchandise-service/               # 商品服务
├── cart-service/                      # 购物车服务
├── order-service/                     # 订单服务
├── payment-service/                   # 支付服务
```

## 技术栈

- **核心框架**: Spring Boot 3.x, Spring Cloud Alibaba
- **服务注册与发现**: Nacos
- **配置中心**: Nacos
- **网关**: Spring Cloud Gateway + nginx
- **数据库**: MySQL, MyBatis Plus, redis
- **安全框架**: Sa-Token
- **构建工具**: Gradle

## 环境配置

### 1. 环境变量配置
每个服务都需要在 `src/main/resources/` 目录下创建 `.env` 文件:

```env
# Nacos配置
NACOS_ADDR=your_nacos_server_address:port
NACOS_USER=your_nacos_username
NACOS_PASSWORD=your_nacos_password
```

### 2. Nacos配置中心设置
需要在Nacos中创建以下配置文件:

1. `authentication-and-authorization-properties` - 认证服务数据库配置
2. `user-service-properties` - 用户服务数据库配置
3. `merchandise-service-properties` - 商品服务数据库配置
4. `cart-service-properties` - 购物车服务数据库配置
5. `order-service-properties` - 订单服务数据库配置
6. `payment-service-properties` - 支付服务数据库配置
7. `sa-token-properties` - Sa-Token配置
8. `application.properties` - 公共配置

### 3. 数据库配置示例
在Nacos配置文件中添加以下内容:

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: ${DB_USER}
    password: ${DB_PASSWORD}
```

### 4. 验证服务
访问 `http://localhost:8081` 查看网关是否正常工作。

## 联系方式

如有问题，请提交Issue或联系项目维护者。