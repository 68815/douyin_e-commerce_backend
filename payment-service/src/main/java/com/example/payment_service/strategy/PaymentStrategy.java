package com.example.payment_service.strategy;

import java.util.Map;

public interface PaymentStrategy {

    String getPaymentMethod();

    Map<String, String> createPaymentOrder(String paymentNo, Long amount, String description, String returnUrl, String notifyUrl);

    boolean verifyCallback(Map<String, String> params);

    String getTransactionId(Map<String, String> params);

    String getPaymentStatus(Map<String, String> params);
}
