package com.example.payment_service.request;

import lombok.Data;

import java.util.List;
/**
 * 购物车结算支付请求Request
 */
@Data
public class CartCheckoutRequest {
    
    private Long userId;                // 用户ID
    private List<Long> cartItemIds;     // 选中的购物车项ID列表
    private String paymentMethod;       // 支付方式
    private String returnUrl;           // 支付成功后跳转地址
    private String notifyUrl;           // 异步通知地址
}