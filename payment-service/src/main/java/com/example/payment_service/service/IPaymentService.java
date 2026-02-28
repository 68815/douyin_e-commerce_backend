package com.example.payment_service.service;

import com.example.payment_service.request.CartCheckoutRequest;
import com.example.payment_service.request.DirectPurchaseRequest;
import com.example.payment_service.VO.OrderPaymentVO;
import com.example.payment_service.VO.PaymentResultVO;

/**
 * 支付服务接口
 */
public interface IPaymentService {

    /**
     * 处理直接购买支付
     */
    PaymentResultVO processDirectPurchase(DirectPurchaseRequest request);

    /**
     * 处理购物车结算支付
     */
    PaymentResultVO processCartCheckout(CartCheckoutRequest request);

    /**
     * 获取订单支付详情（通用）
     */
    OrderPaymentVO getOrderPaymentDetail(String paymentNo);

    /**
     * 查询支付状态
     */
    PaymentResultVO getPaymentStatus(String paymentNo);

    /**
     * 处理支付回调
     */
    boolean handlePaymentCallback(String callbackData);

    /**
     * 定时取消未完成的支付
     */
    void cancelPendingPayments();
}