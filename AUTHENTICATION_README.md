# 抖音电商后台认证系统

基于 SaToken 框架实现的完整登录认证系统，支持动态白名单、黑名单管理和用户封禁功能。

## 功能特性

### 1. 登录认证
- 用户登录/注册/登出
- JWT Token 认证
- 密码加密存储（BCrypt）
- 自动续期 Token

### 2. 白名单管理
- 静态白名单配置（application-auth.yml）
- 动态白名单管理（运行时可修改）
- 支持通配符路径匹配
- **支持正则表达式模式匹配**

### 3. 黑名单管理
- 路径黑名单前缀
- 用户封禁系统
- 支持定时封禁和解封

### 4. 权限控制
- 基于注解的权限校验
- 管理员角色权限
- 灵活的权限配置

## 快速开始

### 1. 添加依赖
项目已包含 SaToken 相关依赖：
```gradle
implementation("cn.dev33:sa-token-spring-boot3-starter:1.38.0")
implementation("cn.dev33:sa-token-redis:1.38.0")
```

### 2. 配置说明

#### 主配置文件 (application.properties)
```properties
# 包含认证配置
spring.config.import=application-auth.yml
```

#### 认证配置文件 (application-auth.yml)
```yaml
sa-token:
  token-name: satoken
  timeout: 2592000  # 30天
  activity-timeout: -1
  is-concurrent: true
  is-share: false
  token-style: uuid
  is-log: true

auth:
  white-list:
    - /auth/login
    - /auth/register
    - /doc.html
    - /swagger-ui/**
    - /v3/api-docs/**
    - /webjars/**
    - /favicon.ico
    - /error

  black-list:
    prefix:
      - /admin/
    users: []
```

## API 接口

### 认证接口 (AuthController)

#### 用户登录
```http
POST /auth/login
Content-Type: application/x-www-form-urlencoded

username=admin&password=123456
```

响应示例：
```json
{
  "success": true,
  "message": "登录成功",
  "token": "satoken_xxxx",
  "userId": 1,
  "username": "admin"
}
```

#### 用户注册
```http
POST /auth/register
Content-Type: application/json

{
  "username": "test",
  "passwordHash": "123456",
  "email": "test@example.com"
}
```

#### 检查登录状态
```http
GET /auth/checkLogin
```

#### 获取用户信息
```http
GET /auth/userInfo
Authorization: Bearer satoken_xxxx
```

### 用户管理接口 (UserController)
需要登录后才能访问：

```http
GET /user/list
Authorization: Bearer satoken_xxxx

GET /user/1
Authorization: Bearer satoken_xxxx

PUT /user/1
Authorization: Bearer satoken_xxxx
Content-Type: application/json

{
  "email": "new@example.com"
}

DELETE /user/1
Authorization: Bearer satoken_xxxx
```

### 配置管理接口 (AuthConfigController)
需要管理员权限：

#### 白名单管理
```http
GET /admin/auth-config/white-list
POST /admin/auth-config/white-list?path=/api/public
DELETE /admin/auth-config/white-list?path=/api/public
```

#### 黑名单管理
```http
GET /admin/auth-config/black-list/prefix
POST /admin/auth-config/black-list/prefix?prefix=/api/secure
DELETE /admin/auth-config/black-list/prefix?prefix=/api/secure
```

#### 用户封禁
```http
POST /admin/auth-config/user-blacklist/1?seconds=3600
DELETE /admin/auth-config/user-blacklist/1
GET /admin/auth-config/user-blacklist/1
```

## 使用示例

### 1. 用户登录
```bash
curl -X POST "http://localhost:8080/auth/login" \
  -d "username=admin&password=123456"
```

### 2. 访问需要认证的接口
```bash
curl -X GET "http://localhost:8080/user/list" \
  -H "Authorization: Bearer satoken_xxxx"
```

### 3. 动态添加白名单
```bash
curl -X POST "http://localhost:8080/admin/auth-config/white-list?path=/api/public" \
  -H "Authorization: Bearer satoken_xxxx"
```

### 4. 封禁用户
```bash
curl -X POST "http://localhost:8080/admin/auth-config/user-blacklist/2?seconds=3600" \
  -H "Authorization: Bearer satoken_xxxx"
```

## 配置自定义

### 修改白名单
编辑 `src/main/resources/application-auth.yml` 文件：
```yaml
auth:
  white-list:
    - /auth/login
    - /auth/register
    - /api/public/**
    - ^/api/v\\d+/public/.*$  # 正则表达式示例：匹配所有版本API的public路径
    - ^/static/.+\\.(css|js|png)$  # 正则表达式示例：匹配静态资源文件
```

### 正则表达式模式使用
白名单支持正则表达式模式匹配，以 `^` 开头的路径将被识别为正则表达式：

#### 示例正则表达式模式：
- `^/api/v\\d+/.*$` - 匹配所有版本的API路径
- `^/static/.+\\.(css|js|png)$` - 匹配CSS、JS、PNG静态文件
- `^/public/.+` - 匹配所有public路径下的资源
- `^/health(/.*)?$` - 匹配健康检查相关路径

#### 动态添加正则表达式白名单：
```bash
curl -X POST "http://localhost:8080/admin/auth-config/white-list?path=^/api/v\\d+/public/.*$" \
  -H "Authorization: Bearer satoken_xxxx"
```

#### 检查正则表达式匹配：
```bash
curl -X GET "http://localhost:8080/admin/auth-config/check-white-list?path=/api/v1/public/test" \
  -H "Authorization: Bearer satoken_xxxx"
```

响应示例：
```json
{
  "isWhiteList": true,
  "path": "/api/v1/public/test"
}
```    - /static/**
```

### 修改黑名单前缀
```yaml
auth:
  black-list:
    prefix:
      - /admin/
      - /secure/
```

### 修改 Token 配置
```yaml
sa-token:
  timeout: 86400  # 1天
  activity-timeout: 1800  # 30分钟无操作过期
  is-concurrent: false  # 不允许并发登录
```

## 注意事项

1. **密码安全**: 用户密码使用 BCrypt 加密存储，确保安全
2. **Token 安全**: 建议在生产环境使用 HTTPS 传输 Token
3. **Redis 集成**: 如需分布式会话，请配置 Redis
4. **权限控制**: 管理员接口需要 `admin` 角色权限
5. **动态配置**: 白名单和黑名单支持运行时动态修改

## 故障排除

### 1. 认证失败
- 检查 Token 是否有效
- 检查用户是否被封禁
- 查看 SaToken 日志输出

### 2. 权限不足
- 检查用户角色权限
- 验证管理员权限配置

### 3. 配置不生效
- 检查 application-auth.yml 加载顺序
- 重启应用使配置生效

## 技术支持

如有问题请参考：
- [SaToken 官方文档](https://sa-token.cc/)
- [Spring Security 最佳实践](https://spring.io/projects/spring-security)