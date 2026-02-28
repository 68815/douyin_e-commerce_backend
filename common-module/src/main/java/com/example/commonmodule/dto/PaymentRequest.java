package com.example.commonmodule.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付请求DTO
 */
@Data
public class PaymentRequest {
    
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod; // alipay, wechat, bank_card
    private String returnUrl; // 支付成功后跳转地址
    private String notifyUrl; // 异步通知地址
}