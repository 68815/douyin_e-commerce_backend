package com.example.order_service.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.order_service.DO.Order;
import com.example.order_service.DO.OrderItem;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.enums.OrderStatus;
import com.example.order_service.mapper.OrderItemMapper;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.service.IOrderService;
import com.example.order_service.utils.OrderNoGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private OrderNoGenerator orderNoGenerator;

    @Autowired
    private OrderItemMapper orderItemMapper;

    private static final int ORDER_EXPIRE_MINUTES = 30;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long userId, CreateOrderRequest request) {
        log.info("开始创建订单，用户ID: {}", userId);

        if (request.getIdempotentKey() != null && !request.getIdempotentKey().isEmpty()) {
            LambdaQueryWrapper<Order> idempotentCheck = new LambdaQueryWrapper<>();
            idempotentCheck.eq(Order::getIdempotentKey, request.getIdempotentKey());
            Order existingOrder = this.getOne(idempotentCheck);
            if (existingOrder != null) {
                log.info("幂等检查：订单已存在，订单号: {}", existingOrder.getOrderNo());
                return existingOrder;
            }
        }

        String orderNo = orderNoGenerator.generateOrderNo();
        log.info("生成的订单号: {}", orderNo);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderRequest.CreateOrderItemRequest itemRequest : request.getItems()) {
            BigDecimal subtotal = itemRequest.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemRequest.getProductId());
            orderItem.setProductName(itemRequest.getProductName());
            orderItem.setProductImage(itemRequest.getProductImage());
            orderItem.setSkuInfo(itemRequest.getSkuInfo());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(itemRequest.getPrice());
            orderItem.setSubtotal(subtotal);
            orderItems.add(orderItem);
        }

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setIdempotentKey(request.getIdempotentKey());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING_PAYMENT.getCode());
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        boolean saved = this.save(order);
        if (!saved) {
            log.error("订单保存失败");
            return null;
        }

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        log.info("订单创建成功，订单ID: {}, 订单号: {}", order.getId(), orderNo);
        return order;
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        log.info("获取用户订单列表，用户ID: {}", userId);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId)
               .orderByDesc(Order::getCreatedAt);
        return this.list(wrapper);
    }

    @Override
    public Order getOrderDetail(Long orderId, Long userId) {
        log.info("获取订单详情，订单ID: {}, 用户ID: {}", orderId, userId);
        Order order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            log.warn("订单不存在或不属于该用户，订单ID: {}, 用户ID: {}", orderId, userId);
            return null;
        }
        return order;
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId, Long userId) {
        log.info("获取订单商品列表，订单ID: {}, 用户ID: {}", orderId, userId);
        Order order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            log.warn("订单不存在或不属于该用户，订单ID: {}, 用户ID: {}", orderId, userId);
            return new ArrayList<>();
        }

        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        return orderItemMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId, Long userId) {
        log.info("取消订单，订单ID: {}, 用户ID: {}", orderId, userId);
        Order order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            log.warn("订单不存在或不属于该用户，订单ID: {}, 用户ID: {}", orderId, userId);
            return false;
        }

        if (order.getStatus().equals(OrderStatus.PENDING_PAYMENT.getCode())) {
            order.setStatus(OrderStatus.CANCELLED.getCode());
            order.setCancelledAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            return this.updateById(order);
        }

        log.warn("订单状态不允许取消，订单ID: {}, 当前状态: {}", orderId, order.getStatus());
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(Long orderId, Long userId, String paymentMethod) {
        log.info("支付订单，订单ID: {}, 用户ID: {}, 支付方式: {}", orderId, userId, paymentMethod);
        Order order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            log.warn("订单不存在或不属于该用户，订单ID: {}, 用户ID: {}", orderId, userId);
            return false;
        }

        if (!order.getStatus().equals(OrderStatus.PENDING_PAYMENT.getCode())) {
            log.warn("订单状态不允许支付，订单ID: {}, 当前状态: {}", orderId, order.getStatus());
            return false;
        }

        order.setStatus(OrderStatus.PAID.getCode());
        order.setPaymentMethod(paymentMethod);
        order.setPaidAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return this.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmOrder(Long orderId, Long userId) {
        log.info("确认收货，订单ID: {}, 用户ID: {}", orderId, userId);
        Order order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            log.warn("订单不存在或不属于该用户，订单ID: {}, 用户ID: {}", orderId, userId);
            return false;
        }

        if (!order.getStatus().equals(OrderStatus.SHIPPED.getCode())) {
            log.warn("订单状态不允许确认收货，订单ID: {}, 当前状态: {}", orderId, order.getStatus());
            return false;
        }

        order.setStatus(OrderStatus.COMPLETED.getCode());
        order.setUpdatedAt(LocalDateTime.now());
        return this.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelUnpaidOrders() {
        log.info("开始执行取消未支付订单定时任务");
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getStatus, OrderStatus.PENDING_PAYMENT.getCode())
               .le(Order::getCreatedAt, LocalDateTime.now().minusMinutes(ORDER_EXPIRE_MINUTES));

        List<Order> unpaidOrders = this.list(wrapper);
        log.info("找到 {} 个未支付订单需要取消", unpaidOrders.size());

        for (Order order : unpaidOrders) {
            order.setStatus(OrderStatus.CANCELLED.getCode());
            order.setCancelledAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            this.updateById(order);
            log.info("已取消未支付订单，订单号: {}", order.getOrderNo());
        }
    }

    @Override
    public boolean existsByOrderNo(String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        return this.count(wrapper) > 0;
    }
}
