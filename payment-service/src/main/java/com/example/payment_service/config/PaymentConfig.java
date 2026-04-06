package com.example.payment_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentConfig {

    private AlipayConfig alipay = new AlipayConfig();

    private WechatpayConfig wechatpay = new WechatpayConfig();

    private RedisConfig redis = new RedisConfig();

    @Data
    public static class AlipayConfig {
        private String appId;
        private String privateKey;
        private String alipayPublicKey;
        private String gateway = "https://openapi.alipay.com/gateway.do";
        private String notifyUrl;
        private String returnUrl;
        private String format = "JSON";
        private String charset = "UTF-8";
        private String signType = "RSA2";
    }

    @Data
    public static class WechatpayConfig {
        private String appId;
        private String mchId;
        private String privateKey;
        private String serialNo;
        private String apiV3Key;
        private String notifyUrl;
        private String certPath;
        private String certPassword;
    }

    @Data
    public static class RedisConfig {
        private String keyPrefix = "payment:idempotency:";
        private int expireSeconds = 3600;
    }
}
