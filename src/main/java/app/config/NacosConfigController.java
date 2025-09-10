package app.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nacos配置控制器
 * 用于动态配置管理和服务发现
 */
@RestController
@RequestMapping("/nacos")
public class NacosConfigController {

    @NacosValue(value = "${nacos.config.example:default}", autoRefreshed = true)
    private String exampleConfig;

    /**
     * 获取Nacos配置示例
     */
    @GetMapping("/config/example")
    public String getExampleConfig() {
        return exampleConfig;
    }

    /**
     * 监听配置变化
     */
    @NacosConfigListener(dataId = "douyin-ecommerce", groupId = "DEFAULT_GROUP")
    public void onConfigChange(String newConfig) {
        System.out.println("配置发生变化: " + newConfig);
        // 这里可以处理配置变更逻辑
    }

    /**
     * 服务健康检查端点
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}