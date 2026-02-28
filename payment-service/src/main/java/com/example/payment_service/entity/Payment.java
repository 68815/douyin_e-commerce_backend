package com.example.payment_service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体类
 */
@Data
@TableName("payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付ID
     */
    @TableId(value = "payment_id", type = IdType.AUTO)
    private Long paymentId;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 支付流水号
     */
    @TableField("payment_no")
    private String paymentNo;

    /**
     * 支付金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 支付方式
     */
    @TableField("payment_method")
    private String paymentMethod;

    /**
     * 支付状态 (0:待支付, 1:支付成功, 2:支付失败, 3:已退款)
     */
    @TableField("payment_status")
    private Integer paymentStatus;

    /**
     * 第三方交易ID
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 支付网关返回信息
     */
    @TableField("gateway_response")
    private String gatewayResponse;

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

    /**
     * 支付完成时间
     */
    @TableField("paid_time")
    private LocalDateTime paidTime;

    /**
     * 退款时间
     */
    @TableField("refund_time")
    private LocalDateTime refundTime;

    /**
     * 支付过期时间
     */
    @TableField("expired_time")
    private LocalDateTime expiredTime;

    @Override
    public String toString() {
        return "Payment{" +
            "paymentId=" + paymentId +
            ", orderId=" + orderId +
            ", userId=" + userId +
            ", paymentNo='" + paymentNo + '\'' +
            ", amount=" + amount +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", paymentStatus=" + paymentStatus +
            ", transactionId='" + transactionId + '\'' +
            ", createdTime=" + createdTime +
            ", updatedTime=" + updatedTime +
            ", paidTime=" + paidTime +
            ", refundTime=" + refundTime +
            ", expiredTime=" + expiredTime +
            '}';
    }
}