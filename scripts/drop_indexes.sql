-- 数据库索引删除脚本（回滚脚本）
-- 谨慎执行！删除索引可能会影响查询性能
-- 执行前请确保有完整的数据库备份

-- ==================== 订单表索引删除 ====================

-- 删除用户订单查询索引
DROP INDEX IF EXISTS idx_order_user_id ON `order`;

-- 删除订单状态查询索引
DROP INDEX IF EXISTS idx_order_status ON `order`;

-- 删除创建时间索引
DROP INDEX IF EXISTS idx_order_created_at ON `order`;

-- 删除用户+状态复合索引
DROP INDEX IF EXISTS idx_order_user_status ON `order`;

-- 删除状态+创建时间复合索引
DROP INDEX IF EXISTS idx_order_status_created_at ON `order`;

-- ==================== 订单项表索引删除 ====================

-- 删除订单项查询索引
DROP INDEX IF EXISTS idx_order_item_order_id ON order_item;

-- 删除产品ID索引
DROP INDEX IF EXISTS idx_order_item_product_id ON order_item;

-- 删除订单+产品复合索引
DROP INDEX IF EXISTS idx_order_item_order_product ON order_item;

-- ==================== 产品表索引删除 ====================

-- 删除库存查询索引
DROP INDEX IF EXISTS idx_product_stock ON product;

-- 删除产品状态索引
DROP INDEX IF EXISTS idx_product_status ON product;

-- ==================== 用户表索引删除 ====================

-- 删除用户名索引
DROP INDEX IF EXISTS idx_user_username ON user;

-- 删除邮箱索引
DROP INDEX IF EXISTS idx_user_email ON user;

-- ==================== 执行说明 ====================

-- 1. 删除顺序建议：
--    先删除复合索引，再删除单列索引
--    先删除使用频率较低的索引

-- 2. 删除索引的影响：
--    相关查询可能会变慢
--    写操作性能可能会提升（减少索引维护开销）

-- 3. 监控建议：
--    删除索引后密切监控系统性能
--    如果性能下降，考虑重新创建必要的索引

-- 4. 备份建议：
--    删除索引前执行完整数据库备份
--    记录删除的索引名称和创建语句

-- 5. 恢复方法：
--    如果需要恢复索引，重新运行 create_indexes.sql 脚本

-- ==================== 紧急恢复 ====================

-- 如果删除索引后系统性能严重下降，立即：
-- 1. 停止删除操作
-- 2. 评估影响范围
-- 3. 逐步重新创建关键索引
-- 4. 监控系统恢复情况

-- 关键索引恢复优先级：
-- 1. idx_order_user_id（用户订单查询）
-- 2. idx_order_item_order_id（订单项查询）
-- 3. idx_order_status_created_at（定时任务）

-- 恢复命令示例：
-- CREATE INDEX idx_order_user_id ON `order`(user_id);
-- CREATE INDEX idx_order_item_order_id ON order_item(order_id);
-- CREATE INDEX idx_order_status_created_at ON `order`(status, created_at);