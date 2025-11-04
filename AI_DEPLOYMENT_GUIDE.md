# AI服务升级部署指南

## 环境要求

### 基础环境
- Java 17+
- MySQL 8.0+
- Maven/Gradle

### 可选组件（根据选择的方案）
- Elasticsearch 8.x（用于智能搜索）
- OpenAI API密钥（用于大语言模型）
- 或其他AI服务提供商API密钥

## 部署步骤

### 1. 依赖安装

```bash
# 重新构建项目（包含新添加的依赖）
./gradlew clean build
```

### 2. Elasticsearch部署（方案1A）

#### Docker方式（推荐）
```bash
# 拉取Elasticsearch镜像
docker pull docker.elastic.co/elasticsearch/elasticsearch:8.13.0

# 运行Elasticsearch
docker run -d --name elasticsearch \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  docker.elastic.co/elasticsearch/elasticsearch:8.13.0
```

#### 本地安装
1. 下载Elasticsearch：https://www.elastic.co/downloads/elasticsearch
2. 解压并配置config/elasticsearch.yml
3. 启动：./bin/elasticsearch

### 3. AI服务配置

#### 环境变量配置
在`.env`文件中添加：

```env
# Elasticsearch配置（如果使用）
ES_HOST=localhost
ES_PORT=9200
ES_USERNAME=elastic
ES_PASSWORD=your_password

# OpenAI配置（如果使用）
OPENAI_API_KEY=your_openai_api_key

# 其他AI服务商配置
QIANFAN_API_KEY=your_qianfan_key
QIANFAN_SECRET_KEY=your_qianfan_secret
DASHSCOPE_API_KEY=your_dashscope_key
```

#### 配置文件选择
在`application.properties`中启用相应的配置：

```properties
# 使用Elasticsearch + OpenAI的混合方案
ai.service.impl=hybrid
ai.search.impl=elasticsearch  
ai.chat.impl=llm

# 或者只启用其中一种
# ai.service.impl=elasticsearch  # 仅智能搜索
# ai.service.impl=llm           # 仅AI客服
```

### 4. 数据同步

首次启动时需要同步数据到Elasticsearch：

```bash
# 启动应用后，调用同步接口
curl -X POST http://localhost:8090/api/ai/sync
```

或者等待定时任务自动同步（每天凌晨2点）。

### 5. 验证部署

#### 验证智能搜索
```bash
# 测试模糊搜索
curl "http://localhost:8090/api/ai/orders?query=ordr"

# 测试拼音搜索  
curl "http://localhost:8090/api/ai/orders?query=dingdan"
```

#### 验证AI客服
```bash
# 测试自然语言对话
curl -X POST http://localhost:8090/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"我的订单什么时候发货？"}'
```

## 性能优化配置

### Elasticsearch优化
```yaml
# application-ai.yml
spring:
  elasticsearch:
    connection-timeout: 2s
    socket-timeout: 5s
    max-connections: 100
    max-connections-per-route: 50
```

### AI服务优化
```yaml
# 调整模型参数
spring:
  ai:
    openai:
      chat:
        options:
          temperature: 0.3      # 更确定的回答
          max-tokens: 500       # 限制响应长度
          top-p: 0.9
```

## 监控和维护

### 健康检查
```bash
# 检查Elasticsearch状态
curl http://localhost:9200/_cluster/health

# 检查AI服务状态  
curl http://localhost:8090/actuator/health
```

### 日志监控
查看应用日志关注：
- Elasticsearch连接状态
- AI API调用成功率
- 搜索性能指标

## 故障排除

### 常见问题

1. **Elasticsearch连接失败**
   - 检查ES是否正常运行：`curl http://localhost:9200`
   - 验证网络连接和防火墙设置

2. **AI API调用失败**
   - 检查API密钥是否正确
   - 验证网络是否能访问外部API
   - 查看调用配额和频率限制

3. **数据不同步**
   - 检查MySQL到ES的数据同步服务
   - 验证定时任务是否正常执行

### 日志分析
查看应用日志文件，关注以下关键词：
- `Connection refused` - 网络连接问题
- `401 Unauthorized` - API密钥错误  
- `429 Too Many Requests` - 调用频率超限
- `Read timed out` - 超时设置需要调整

## 备份和恢复

### Elasticsearch数据备份
```bash
# 创建快照
curl -X PUT "localhost:9200/_snapshot/my_backup/snapshot_1?wait_for_completion=true"

# 恢复快照  
curl -X POST "localhost:9200/_snapshot/my_backup/snapshot_1/_restore"
```

### 配置备份
定期备份以下文件：
- `application-ai.yml` - AI服务配置
- `.env` - 敏感信息配置
- Elasticsearch索引配置

## 扩展方案

### 水平扩展
- 部署Elasticsearch集群
- 使用负载均衡器分发AI请求
- 配置数据库读写分离

### 性能优化
- 添加Redis缓存层
- 优化Elasticsearch索引设计
- 实施请求限流和降级策略

## 安全考虑

1. **API密钥保护**
   - 不要将API密钥提交到代码仓库
   - 使用环境变量或配置中心管理敏感信息

2. **访问控制**
   - 配置Elasticsearch访问权限
   - 实施API调用频率限制

3. **数据加密**
   - 启用HTTPS通信
   - 敏感数据加密存储

## 版本升级

定期检查以下组件的版本更新：
- Spring AI
- Elasticsearch
- 各AI服务商SDK
- 关注安全更新和性能改进