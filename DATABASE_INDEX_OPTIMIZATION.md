# 数据库索引优化指南

## 当前性能问题分析

基于对项目代码的分析，发现以下可能存在性能问题的查询场景：

1. **订单查询**：频繁按用户ID、状态、创建时间查询
2. **结算服务**：按订单ID查询订单项和产品信息
3. **定时任务**：按状态和创建时间查询未支付订单

## 推荐添加的索引

### 1. 订单表 (order)

```sql
-- 用户订单查询索引（高频查询）
CREATE INDEX idx_order_user_id ON `order`(user_id);

-- 订单状态查询索引（用于状态筛选）
CREATE INDEX idx_order_status ON `order`(status);

-- 创建时间索引（用于时间范围查询和排序）
CREATE INDEX idx_order_created_at ON `order`(created_at);

-- 复合索引：用户+状态（常用查询组合）
CREATE INDEX idx_order_user_status ON `order`(user_id, status);

-- 复合索引：状态+创建时间（定时任务优化）
CREATE INDEX idx_order_status_created_at ON `order`(status, created_at);
```

### 2. 订单项表 (order_item)

```sql
-- 订单项查询索引（高频查询）
CREATE INDEX idx_order_item_order_id ON order_item(order_id);

-- 产品ID索引（库存验证和产品查询）
CREATE INDEX idx_order_item_product_id ON order_item(product_id);

-- 复合索引：订单+产品（常用查询组合）
CREATE INDEX idx_order_item_order_product ON order_item(order_id, product_id);
```

### 3. 产品表 (product)

```sql
-- 库存查询索引（库存验证优化）
CREATE INDEX idx_product_stock ON product(stock);

-- 产品状态索引（可用产品查询）
CREATE INDEX idx_product_status ON product(status);
```

### 4. 用户表 (user)

```sql
-- 用户名索引（登录和查询优化）
CREATE INDEX idx_user_username ON user(username);

-- 邮箱索引（登录和查询优化）
CREATE INDEX idx_user_email ON user(email);
```

## 具体查询场景优化

### 场景1：用户订单查询
**原始查询**：`WHERE user_id = ? ORDER BY created_at DESC`
**优化建议**：添加 `idx_order_user_id` 和 `idx_order_created_at` 索引

### 场景2：未支付订单定时取消
**原始查询**：`WHERE status = 0 AND created_at <= ?`
**优化建议**：添加 `idx_order_status_created_at` 复合索引

### 场景3：订单项查询（结算服务）
**原始查询**：`WHERE order_id = ?`
**优化建议**：添加 `idx_order_item_order_id` 索引

### 场景4：库存验证
**原始查询**：多次 `WHERE product_id = ?` 查询
**优化建议**：添加 `idx_order_item_product_id` 和 `idx_product_stock` 索引

## 索引创建优先级

1. **高优先级**（立即创建）：
   - `idx_order_user_id`
   - `idx_order_item_order_id` 
   - `idx_order_status_created_at`

2. **中优先级**（一周内创建）：
   - `idx_order_user_status`
   - `idx_order_item_product_id`
   - `idx_product_stock`

3. **低优先级**（后续优化）：
   - `idx_user_username`
   - `idx_user_email`
   - `idx_order_item_order_product`

## 监控和维护

1. **监控慢查询**：启用MySQL慢查询日志，定期分析
2. **索引使用统计**：使用 `SHOW INDEX STATISTICS` 监控索引使用情况
3. **定期优化**：每月执行一次 `OPTIMIZE TABLE`
4. **索引重建**：当数据变更较大时重建索引

## 注意事项

1. **索引选择性**：优先为高选择性的字段创建索引
2. **避免过度索引**：每个额外的索引都会增加写操作的开销
3. **复合索引顺序**：将等值查询字段放在前面，范围查询字段放在后面
4. **覆盖索引**：尽量让查询只需要访问索引而不需要回表

## 预期性能提升

- 用户订单查询响应时间：减少70-80%
- 结算服务处理时间：减少50-60% 
- 定时任务执行时间：减少60-70%
- 整体数据库负载：降低30-40%

## 后续优化建议

1. 考虑添加数据库读写分离
2. 引入缓存层（Redis）缓存热点数据
3. 实施分库分表策略（当数据量达到千万级别时）
4. 使用数据库连接池优化连接管理