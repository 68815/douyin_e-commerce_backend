package com.example.payment_service.request;

import lombok.Data;

/**
 * 直接购买支付请求Request
 */
@Data
public class DirectPurchaseRequest {
    
    private Long userId;                // 用户ID
    private Long productId;             // 商品ID
    private Integer quantity;           // 购买数量
    private String paymentMethod;       // 支付方式
    private String returnUrl;           // 支付成功后跳转地址
    private String notifyUrl;           // 异步通知地址
}