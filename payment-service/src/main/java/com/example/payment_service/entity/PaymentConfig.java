package com.example.payment_service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付配置实体类
 */
@Data
@TableName("payment_config")
public class PaymentConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(value = "config_id", type = IdType.AUTO)
    private Long configId;

    /**
     * 支付方式
     */
    @TableField("payment_method")
    private String paymentMethod;

    /**
     * 配置键名
     */
    @TableField("config_key")
    private String configKey;

    /**
     * 配置值
     */
    @TableField("config_value")
    private String configValue;

    /**
     * 配置描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否启用 (0:禁用, 1:启用)
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @Override
    public String toString() {
        return "PaymentConfig{" +
            "configId=" + configId +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", configKey='" + configKey + '\'' +
            ", configValue='" + configValue + '\'' +
            ", description='" + description + '\'' +
            ", isEnabled=" + isEnabled +
            ", createdTime=" + createdTime +
            ", updatedTime=" + updatedTime +
            '}';
    }
}