package com.example.payment_service.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WechatPayStrategy implements PaymentStrategy {

    @Value("${payment.wechatpay.app-id:}")
    private String appId;

    @Value("${payment.wechatpay.mch-id:}")
    private String mchId;

    @Value("${payment.wechatpay.api-key:}")
    private String apiKey;

    @Value("${payment.wechatpay.notify-url:}")
    private String notifyUrl;

    @Override
    public String getPaymentMethod() {
        return "WECHAT";
    }

    @Override
    public Map<String, String> createPaymentOrder(String paymentNo, Long amount, String description, String returnUrl, String notifyUrlStr) {
        try {
            Map<String, String> result = new HashMap<>();
            result.put("paymentNo", paymentNo);
            result.put("amount", String.valueOf(amount));
            result.put("message", "微信支付集成待完成");
            return result;
        } catch (Exception e) {
            throw new RuntimeException("创建微信支付订单失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean verifyCallback(Map<String, String> params) {
        return true;
    }

    @Override
    public String getTransactionId(Map<String, String> params) {
        return params.get("transaction_id");
    }

    @Override
    public String getPaymentStatus(Map<String, String> params) {
        String tradeState = params.get("trade_state");
        if ("SUCCESS".equals(tradeState)) {
            return "SUCCESS";
        }
        return tradeState;
    }
}
