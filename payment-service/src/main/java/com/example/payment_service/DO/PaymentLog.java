package com.example.payment_service.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付日志实体类
 */
@Data
@TableName("payment_log")
public class PaymentLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /**
     * 支付ID
     */
    @TableField("payment_id")
    private Long paymentId;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 日志类型
     */
    @TableField("log_type")
    private String logType;

    /**
     * 日志级别
     */
    @TableField("log_level")
    private String logLevel;

    /**
     * 操作类型
     */
    @TableField("operation")
    private String operation;

    /**
     * 请求数据
     */
    @TableField("request_data")
    private String requestData;

    /**
     * 响应数据
     */
    @TableField("response_data")
    private String responseData;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 用户代理
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    @Override
    public String toString() {
        return "PaymentLog{" +
            "logId=" + logId +
            ", paymentId=" + paymentId +
            ", orderId=" + orderId +
            ", logType='" + logType + '\'' +
            ", logLevel='" + logLevel + '\'' +
            ", operation='" + operation + '\'' +
            ", requestData='" + requestData + '\'' +
            ", responseData='" + responseData + '\'' +
            ", errorMessage='" + errorMessage + '\'' +
            ", ipAddress='" + ipAddress + '\'' +
            ", userAgent='" + userAgent + '\'' +
            ", createdTime=" + createdTime +
            '}';
    }
}