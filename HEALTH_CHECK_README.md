# 健康检查功能说明

## 概述

本系统提供了完整的健康检查功能，包括基础健康检查、详细系统状态检查、数据库连接检查等。

## 健康检查端点

### 1. 基础健康检查
- **端点**: `GET /health`
- **功能**: 返回应用基础状态信息
- **响应示例**:
```json
{
  "status": "UP",
  "service": "douyin-e-commerce-backend",
  "timestamp": 1721467890123,
  "version": "1.0.0"
}
```

### 2. 详细健康检查
- **端点**: `GET /health/detailed`
- **功能**: 返回详细的系统状态信息，包括数据库状态、内存使用情况等
- **响应示例**:
```json
{
  "application": "UP",
  "timestamp": 1721467890123,
  "database": {
    "status": "UP",
    "message": "数据库连接正常"
  },
  "memory": {
    "total": 134217728,
    "free": 67108864,
    "max": 2147483648,
    "used": 67108864
  },
  "system": {
    "availableProcessors": 8
  }
}
```

### 3. 数据库信息检查
- **端点**: `GET /health/database`
- **功能**: 返回数据库连接详细信息
- **响应示例**:
```json
{
  "timestamp": 1721467890123,
  "database": {
    "version": "8.0.33",
    "status": "CONNECTED"
  }
}
```

### 4. 就绪检查
- **端点**: `GET /health/ready`
- **功能**: 检查应用是否就绪接收流量
- **响应示例**:
```json
{
  "status": "READY",
  "timestamp": 1721467890123
}
```

### 5. 存活检查
- **端点**: `GET /health/live`
- **功能**: 检查应用是否存活
- **响应示例**:
```json
{
  "status": "LIVE",
  "timestamp": 1721467890123
}
```

## Spring Boot Actuator 端点

系统还启用了Spring Boot Actuator的健康检查端点：

- `GET /actuator/health` - 完整的健康检查信息
- `GET /actuator/info` - 应用信息
- `GET /actuator/metrics` - 系统指标

## 配置说明

在 `application.properties` 中配置了以下健康检查相关设置：

```properties
# 暴露健康检查端点
management.endpoints.web.exposure.include=health,info,metrics

# 显示健康检查详细信息
management.endpoint.health.show-details=always

# 启用探针检查
management.endpoint.health.probes.enabled=true
```

## 使用场景

1. **容器编排**: Kubernetes等平台可以使用就绪和存活检查来管理Pod生命周期
2. **负载均衡**: 负载均衡器可以使用健康检查来路由流量
3. **监控告警**: 监控系统可以定期检查健康状态并触发告警
4. **故障排查**: 开发人员可以使用详细健康信息进行问题诊断

## 自定义扩展

可以通过实现 `HealthIndicator` 接口来添加自定义的健康检查逻辑，例如：
- 检查外部API可用性
- 检查缓存服务状态
- 检查消息队列连接
- 检查文件系统空间

现有的 `CustomHealthIndicator` 类提供了扩展模板。