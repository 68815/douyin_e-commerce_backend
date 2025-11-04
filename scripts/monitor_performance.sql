-- 数据库性能监控脚本
-- 用于监控索引使用情况和查询性能

-- ==================== 索引使用情况监控 ====================

-- 查看所有表的索引统计信息
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    SEQ_IN_INDEX,
    COLUMN_NAME,
    CARDINALITY,
    INDEX_TYPE,
    IS_VISIBLE
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = DATABASE()
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;

-- ==================== 慢查询监控 ====================

-- 查看当前慢查询配置
SHOW VARIABLES LIKE 'slow_query_log%';
SHOW VARIABLES LIKE 'long_query_time';

-- 启用慢查询日志（如果未启用）
-- SET GLOBAL slow_query_log = 'ON';
-- SET GLOBAL long_query_time = 2; -- 设置慢查询阈值（秒）

-- 查看最近的慢查询
-- SELECT * FROM mysql.slow_log ORDER BY start_time DESC LIMIT 10;

-- ==================== 索引使用统计 ====================

-- 查看索引使用情况（需要MySQL 8.0+）
SELECT 
    OBJECT_SCHEMA,
    OBJECT_NAME,
    INDEX_NAME,
    COUNT_READ,
    COUNT_FETCH,
    COUNT_INSERT,
    COUNT_UPDATE,
    COUNT_DELETE
FROM performance_schema.table_io_waits_summary_by_index_usage
WHERE OBJECT_SCHEMA = DATABASE()
ORDER BY COUNT_READ DESC;

-- ==================== 表大小和索引大小 ====================

-- 查看所有表的大小和索引大小
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    ROUND(DATA_LENGTH/1024/1024, 2) AS 'Data Size (MB)',
    ROUND(INDEX_LENGTH/1024/1024, 2) AS 'Index Size (MB)',
    ROUND((DATA_LENGTH + INDEX_LENGTH)/1024/1024, 2) AS 'Total Size (MB)'
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = DATABASE()
ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;

-- ==================== 当前连接和查询状态 ====================

-- 查看当前正在执行的查询
SHOW PROCESSLIST;

-- 查看当前锁状态
SHOW ENGINE INNODB STATUS\G

-- ==================== 性能计数器 ====================

-- 查看InnoDB状态
SHOW STATUS LIKE 'Innodb%';

-- 查看查询缓存状态
SHOW STATUS LIKE 'Qcache%';

-- 查看连接相关状态
SHOW STATUS LIKE 'Threads%';
SHOW STATUS LIKE 'Connections%';

-- ==================== 索引优化建议 ====================

-- 分析表（更新统计信息）
ANALYZE TABLE `order`, order_item, product, user;

-- 检查表状态
CHECK TABLE `order`, order_item, product, user;

-- ==================== 常用监控命令 ====================

-- 查看数据库版本
SELECT VERSION();

-- 查看字符集配置
SHOW VARIABLES LIKE 'character_set%';
SHOW VARIABLES LIKE 'collation%';

-- 查看缓冲池状态
SHOW VARIABLES LIKE 'innodb_buffer_pool_size';
SHOW STATUS LIKE 'Innodb_buffer_pool%';

-- ==================== 自动化监控建议 ====================

-- 建议设置定期任务：
-- 1. 每天执行 ANALYZE TABLE 更新统计信息
-- 2. 每周检查一次索引使用情况
-- 3. 每月执行 OPTIMIZE TABLE 优化表空间
-- 4. 监控慢查询日志，定期分析优化

-- 示例定时任务（Linux crontab）：
-- 0 2 * * * mysql -uusername -ppassword -e "ANALYZE TABLE order, order_item, product, user" database_name
-- 0 3 * * 0 mysql -uusername -ppassword -e "CHECK TABLE order, order_item, product, user" database_name