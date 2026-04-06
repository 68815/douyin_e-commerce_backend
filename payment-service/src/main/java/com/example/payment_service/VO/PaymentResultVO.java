package com.example.payment_service.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付结果VO
 * 通用的支付结果返回对象
 */
@Data
public class PaymentResultVO {
    
    private Boolean success;
    private String message;
    private String paymentNo;
    private String orderNo;
    private Long userId;
    private BigDecimal amount;
    private Integer paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private String payUrl;
    private Map<String, String> data;
    
    public static PaymentResultVO success(String message, Map<String, String> data) {
        PaymentResultVO vo = new PaymentResultVO();
        vo.setSuccess(true);
        vo.setMessage(message);
        vo.setData(data);
        if (data != null) {
            vo.setPaymentNo(data.get("paymentNo"));
            if (data.get("amount") != null) {
                vo.setAmount(new BigDecimal(data.get("amount")));
            }
        }
        return vo;
    }

    public static PaymentResultVO success(String paymentNo, String orderNo, BigDecimal amount) {
        PaymentResultVO vo = new PaymentResultVO();
        vo.setSuccess(true);
        vo.setMessage("支付成功");
        vo.setPaymentNo(paymentNo);
        vo.setOrderNo(orderNo);
        vo.setAmount(amount);
        vo.setPaymentStatus(1);
        return vo;
    }
    
    public static PaymentResultVO pending(String paymentNo, String orderNo, BigDecimal amount, String payUrl) {
        PaymentResultVO vo = new PaymentResultVO();
        vo.setSuccess(true);
        vo.setMessage("等待支付");
        vo.setPaymentNo(paymentNo);
        vo.setOrderNo(orderNo);
        vo.setAmount(amount);
        vo.setPaymentStatus(0);
        vo.setPayUrl(payUrl);
        return vo;
    }
    
    public static PaymentResultVO failed(String message) {
        PaymentResultVO vo = new PaymentResultVO();
        vo.setSuccess(false);
        vo.setMessage(message);
        vo.setPaymentStatus(2);
        return vo;
    }
}