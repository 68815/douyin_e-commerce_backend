package com.example.order_service.service.Impl;


import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_service.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


    @Override
    public Order createOrder(Long userId, List<Long> cartIds) {
        return null;
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return List.of();
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        return false;
    }

    @Override
    public boolean payOrder(Long orderId, String paymentMethod) {
        return false;
    }

    @Override
    public Order getOrderDetail(Long orderId) {
        return null;
    }

    @Override
    public void cancelUnpaidOrders() {

    }
}