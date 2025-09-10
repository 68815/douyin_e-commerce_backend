package app.service;

/**
 * 支付服务接口
 */
public interface IPaymentService {

    /**
     * 发起支付
     */
    boolean initiatePayment(Long orderId, String paymentMethod);

    /**
     * 取消支付
     */
    boolean cancelPayment(Long orderId);

    /**
     * 查询支付状态
     */
    String getPaymentStatus(Long orderId);

    /**
     * 定时取消未完成的支付
     */
    void cancelPendingPayments();

    /**
     * 处理支付回调
     */
    boolean handlePaymentCallback(String paymentId, String status);
}