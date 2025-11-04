package app.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义健康指示器
 */
@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        // 这里可以添加自定义的健康检查逻辑
        // 例如检查外部服务、缓存状态等
        
        Map<String, Object> details = new HashMap<>();
        details.put("custom", "healthy");
        details.put("timestamp", System.currentTimeMillis());
        
        // 模拟一些检查逻辑
        boolean isHealthy = true;
        
        if (isHealthy) {
            return Health.up().withDetails(details).build();
        } else {
            return Health.down().withDetails(details).build();
        }
    }
}