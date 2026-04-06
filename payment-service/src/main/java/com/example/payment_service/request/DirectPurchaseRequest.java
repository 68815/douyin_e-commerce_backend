package com.example.payment_service.request;

import lombok.Data;

/**
 * 直接购买支付请求Request
 */
@Data
public class DirectPurchaseRequest {

    private Long userId;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String paymentMethod;
    private String returnUrl;
    private String notifyUrl;
}
