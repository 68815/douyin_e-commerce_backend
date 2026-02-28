package com.example.payment_service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款记录实体类
 */
@Data
@TableName("refund_record")
public class RefundRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退款ID
     */
    @TableId(value = "refund_id", type = IdType.AUTO)
    private Long refundId;

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
     * 退款流水号
     */
    @TableField("refund_no")
    private String refundNo;

    /**
     * 退款金额
     */
    @TableField("refund_amount")
    private BigDecimal refundAmount;

    /**
     * 退款原因
     */
    @TableField("refund_reason")
    private String refundReason;

    /**
     * 退款状态 (0:处理中, 1:退款成功, 2:退款失败)
     */
    @TableField("refund_status")
    private Integer refundStatus;

    /**
     * 第三方退款交易ID
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 退款网关返回信息
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
     * 退款完成时间
     */
    @TableField("completed_time")
    private LocalDateTime completedTime;

    @Override
    public String toString() {
        return "RefundRecord{" +
            "refundId=" + refundId +
            ", paymentId=" + paymentId +
            ", orderId=" + orderId +
            ", refundNo='" + refundNo + '\'' +
            ", refundAmount=" + refundAmount +
            ", refundReason='" + refundReason + '\'' +
            ", refundStatus=" + refundStatus +
            ", transactionId='" + transactionId + '\'' +
            ", createdTime=" + createdTime +
            ", updatedTime=" + updatedTime +
            ", completedTime=" + completedTime +
            '}';
    }
}