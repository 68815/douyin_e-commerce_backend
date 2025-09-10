package app.service.impl;

import app.service.IPaymentService;
import app.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 支付服务实现类
 */
@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private IOrderService orderService;

    @Override
    public boolean initiatePayment(Long orderId, String paymentMethod) {
        // 模拟支付处理
        try {
            Thread.sleep(1000); // 模拟支付处理时间
            
            // 模拟支付成功率（90%）
            Random random = new Random();
            if (random.nextInt(100) < 90) {
                return orderService.payOrder(orderId, paymentMethod);
            } else {
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean cancelPayment(Long orderId) {
        // 取消支付逻辑
        return orderService.cancelOrder(orderId);
    }

    @Override
    public String getPaymentStatus(Long orderId) {
        // 获取订单状态
        var order = orderService.getOrderDetail(orderId);
        if (order == null) {
            return "NOT_FOUND";
        }
        
        switch (order.getStatus()) {
            case 0: return "PENDING";
            case 1: return "PAID";
            case 2: return "CANCELLED";
            default: return "UNKNOWN";
        }
    }

    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    @Override
    public void cancelPendingPayments() {
        // 这里可以添加取消长时间未完成支付的逻辑
        // 例如：取消创建时间超过30分钟的待支付订单
    }

    @Override
    public boolean handlePaymentCallback(String paymentId, String status) {
        // 处理支付回调
        // 这里可以集成真实的支付网关回调处理
        return "SUCCESS".equals(status);
    }
}