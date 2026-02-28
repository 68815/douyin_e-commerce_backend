package com.example.payment_service.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款请求DTO
 */
@Data
public class RefundRequest {
    
    private Long paymentId;
    private Long orderId;
    private BigDecimal refundAmount;
    private String refundReason;
}