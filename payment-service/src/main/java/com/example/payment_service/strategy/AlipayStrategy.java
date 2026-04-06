package com.example.payment_service.strategy;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AlipayStrategy implements PaymentStrategy {

    @Value("${payment.alipay.app-id:}")
    private String appId;

    @Value("${payment.alipay.private-key:}")
    private String privateKey;

    @Value("${payment.alipay.alipay-public-key:}")
    private String alipayPublicKey;

    @Value("${payment.alipay.gateway:https://openapi.alipay.com/gateway.do}")
    private String gateway;

    @Value("${payment.alipay.notify-url:}")
    private String notifyUrl;

    @Value("${payment.alipay.return-url:}")
    private String returnUrl;

    @Value("${payment.alipay.format:JSON}")
    private String format;

    @Value("${payment.alipay.charset:UTF-8}")
    private String charset;

    @Value("${payment.alipay.sign-type:RSA2}")
    private String signType;

    @Override
    public String getPaymentMethod() {
        return "ALIPAY";
    }

    @Override
    public Map<String, String> createPaymentOrder(String paymentNo, Long amount, String description, String returnUrlStr, String notifyUrlStr) {
        try {
            AlipayConfig alipayConfig = new AlipayConfig();
            alipayConfig.setAppId(appId);
            alipayConfig.setPrivateKey(privateKey);
            alipayConfig.setAlipayPublicKey(alipayPublicKey);
            alipayConfig.setServerUrl(gateway);
            alipayConfig.setFormat(format);
            alipayConfig.setCharset(charset);
            alipayConfig.setSignType(signType);

            DefaultAlipayClient client = new DefaultAlipayClient(alipayConfig);

            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setReturnUrl(returnUrlStr != null ? returnUrlStr : returnUrl);
            request.setNotifyUrl(notifyUrlStr != null ? notifyUrlStr : notifyUrl);

            String bizContent = String.format(
                "{\"out_trade_no\":\"%s\",\"total_amount\":\"%s\",\"subject\":\"%s\",\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}",
                paymentNo, amount / 100.0, description
            );
            request.setBizContent(bizContent);

            String form = client.pageExecute(request).getBody();
            return Map.of(
                "form", form,
                "paymentNo", paymentNo,
                "amount", String.valueOf(amount)
            );
        } catch (AlipayApiException e) {
            throw new RuntimeException("创建支付宝订单失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean verifyCallback(Map<String, String> params) {
        try {
            AlipayConfig alipayConfig = new AlipayConfig();
            alipayConfig.setAlipayPublicKey(alipayPublicKey);
            alipayConfig.setCharset(charset);
            alipayConfig.setSignType(signType);

            return AlipaySignature.rsaCheckV1(
                params,
                alipayPublicKey,
                charset,
                signType
            );
        } catch (AlipayApiException e) {
            return false;
        }
    }

    @Override
    public String getTransactionId(Map<String, String> params) {
        return params.get("trade_no");
    }

    @Override
    public String getPaymentStatus(Map<String, String> params) {
        String tradeStatus = params.get("trade_status");
        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
            return "SUCCESS";
        }
        return tradeStatus;
    }
}
