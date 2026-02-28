package com.example.payment_service.VO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付结果VO
 * 通用的支付结果返回对象
 */
@Data
public class PaymentResultVO {
    
    private Boolean success;            // 支付是否成功
    private String message;             // 结果消息
    private String paymentNo;           // 支付流水号
    private String orderNo;             // 订单号
    private Long userId;                // 用户ID
    private BigDecimal amount;          // 支付金额
    private Integer paymentStatus;      // 支付状态
    private String paymentMethod;       // 支付方式
    private String transactionId;       // 第三方交易ID
    private String payUrl;              // 支付链接（如果是待支付状态）
    
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