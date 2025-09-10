package app.service;

import app.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 订单服务接口
 */
public interface IOrderService extends IService<Order> {

    /**
     * 创建订单
     */
    Order createOrder(Long userId, List<Long> cartIds);

    /**
     * 获取用户订单列表
     */
    List<Order> getUserOrders(Long userId);

    /**
     * 取消订单
     */
    boolean cancelOrder(Long orderId);

    /**
     * 支付订单
     */
    boolean payOrder(Long orderId, String paymentMethod);

    /**
     * 获取订单详情
     */
    Order getOrderDetail(Long orderId);

    /**
     * 定时取消未支付订单
     */
    void cancelUnpaidOrders();
}