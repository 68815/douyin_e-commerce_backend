
package app.controller;

import app.service.impl.DatabaseHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@RestController
public class HealthController {
    
    @Autowired
    private DatabaseHealthService databaseHealthService;
    
    /**
     * 基础健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "douyin-e-commerce-backend");
        result.put("timestamp", System.currentTimeMillis());
        result.put("version", "1.0.0");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 详细健康检查
     */
    @GetMapping("/health/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealthCheck() {
        Map<String, Object> result = new HashMap<>();
        
        // 检查应用状态
        result.put("application", "UP");
        result.put("timestamp", System.currentTimeMillis());
        
        // 检查数据库状态
        result.put("database", databaseHealthService.checkDatabaseHealth());
        
        // 检查内存状态
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> memory = new HashMap<>();
        memory.put("total", runtime.totalMemory());
        memory.put("free", runtime.freeMemory());
        memory.put("max", runtime.maxMemory());
        memory.put("used", runtime.totalMemory() - runtime.freeMemory());
        result.put("memory", memory);
        
        // 检查系统负载
        Map<String, Object> system = new HashMap<>();
        system.put("availableProcessors", runtime.availableProcessors());
        result.put("system", system);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 就绪检查
     */
    @GetMapping("/health/ready")
    public ResponseEntity<Map<String, Object>> readinessCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "READY");
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }
    
    /**
     * 存活检查
     */
    @GetMapping("/health/live")
    public ResponseEntity<Map<String, Object>> livenessCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "LIVE");
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }
    
    /**
     * 数据库信息检查
     */
    @GetMapping("/health/database")
    public ResponseEntity<Map<String, Object>> databaseInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", System.currentTimeMillis());
        result.put("database", databaseHealthService.getDatabaseInfo());
        return ResponseEntity.ok(result);
    }
}