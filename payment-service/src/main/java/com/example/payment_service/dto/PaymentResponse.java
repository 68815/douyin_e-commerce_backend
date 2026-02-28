package com.example.payment_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付响应DTO
 */
@Data
public class PaymentResponse {
    
    private Long paymentId;
    private String paymentNo;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private Integer paymentStatus; // 0:待支付, 1:支付成功, 2:支付失败, 3:已退款
    private String transactionId;
    private String payUrl; // 支付链接
    private LocalDateTime createdTime;
    private LocalDateTime expiredTime;
}