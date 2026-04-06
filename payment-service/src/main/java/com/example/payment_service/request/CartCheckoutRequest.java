package com.example.payment_service.request;

import lombok.Data;

import java.util.List;

/**
 * 购物车结算支付请求Request
 */
@Data
public class CartCheckoutRequest {

    private Long userId;
    private Long orderId;
    private List<Long> cartItemIds;
    private String paymentMethod;
    private String returnUrl;
    private String notifyUrl;
}
