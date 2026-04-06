package com.example.order_service.service;

import com.example.order_service.DO.Order;
import com.example.order_service.DO.OrderItem;
import com.example.order_service.dto.CreateOrderRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IOrderService extends IService<Order> {

    Order createOrder(Long userId, CreateOrderRequest request);

    List<Order> getUserOrders(Long userId);

    Order getOrderDetail(Long orderId, Long userId);

    List<OrderItem> getOrderItems(Long orderId, Long userId);

    boolean cancelOrder(Long orderId, Long userId);

    boolean payOrder(Long orderId, Long userId, String paymentMethod);

    boolean confirmOrder(Long orderId, Long userId);

    void cancelUnpaidOrders();

    boolean existsByOrderNo(String orderNo);
}
