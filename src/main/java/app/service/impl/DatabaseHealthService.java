package app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

/**
 * 数据库健康检查服务
 */
@Service
public class DatabaseHealthService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 检查数据库连接状态
     */
    public Map<String, Object> checkDatabaseHealth() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 执行简单的查询来检查数据库连接
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            result.put("status", "UP");
            result.put("message", "数据库连接正常");
        } catch (Exception e) {
            result.put("status", "DOWN");
            result.put("message", "数据库连接异常: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());
        }
        
        return result;
    }
    
    /**
     * 获取数据库连接信息
     */
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> info = new HashMap<>();
        
        try {
            // 获取数据库版本信息
            String version = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
            info.put("version", version);
            info.put("status", "CONNECTED");
        } catch (Exception e) {
            info.put("status", "DISCONNECTED");
            info.put("error", e.getMessage());
        }
        
        return info;
    }
}