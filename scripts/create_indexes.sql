-- 数据库索引优化脚本
-- 执行此脚本前请先备份数据库
-- 建议在业务低峰期执行

-- ==================== 订单表索引 ====================

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

-- ==================== 订单项表索引 ====================

-- 订单项查询索引（高频查询）
CREATE INDEX idx_order_item_order_id ON order_item(order_id);

-- 产品ID索引（库存验证和产品查询）
CREATE INDEX idx_order_item_product_id ON order_item(product_id);

-- 复合索引：订单+产品（常用查询组合）
CREATE INDEX idx_order_item_order_product ON order_item(order_id, product_id);

-- ==================== 产品表索引 ====================

-- 库存查询索引（库存验证优化）
CREATE INDEX idx_product_stock ON product(stock);

-- 产品状态索引（可用产品查询）
CREATE INDEX idx_product_status ON product(status);

-- ==================== 用户表索引 ====================

-- 用户名索引（登录和查询优化）
CREATE INDEX idx_user_username ON user(username);

-- 邮箱索引（登录和查询优化）
CREATE INDEX idx_user_email ON user(email);

-- ==================== 执行说明 ====================

-- 1. 高优先级索引（立即执行）：
--    idx_order_user_id
--    idx_order_item_order_id
--    idx_order_status_created_at

-- 2. 执行顺序建议：
--    先执行高优先级索引
--    监控系统性能后再执行中低优先级索引

-- 3. 索引创建时间预估：
--    小表（<100万行）：几秒到几分钟
--    大表（>1000万行）：可能需要数分钟到数小时

-- 4. 监控命令：
--    SHOW PROCESSLIST; -- 查看当前执行状态
--    SHOW INDEX FROM table_name; -- 查看表索引

-- 5. 回滚脚本（如果需要删除索引）：
--    DROP INDEX index_name ON table_name;