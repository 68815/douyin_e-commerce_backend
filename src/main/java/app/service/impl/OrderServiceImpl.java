package app.service.impl;

import app.entity.*;
import app.mapper.OrderItemMapper;
import app.mapper.OrderMapper;
import app.service.IOrderService;
import app.service.ICartService;
import app.service.IProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ICartService cartService;

    @Autowired
    private IProductService productService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public Order createOrder(Long userId, List<Long> cartIds) {
        // 获取购物车商品
        List<Cart> cartItems = cartService.listByIds(cartIds);
        
        // 计算总金额
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0); // 0: 待支付
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        save(order);

        // 创建订单项
        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setSubtotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItemMapper.insert(orderItem);
        }

        // 清空购物车
        cartService.removeByIds(cartIds);

        return order;
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId)
                   .orderByDesc(Order::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long orderId) {
        Order order = getById(orderId);
        if (order == null || order.getStatus() != 0) {
            return false;
        }

        order.setStatus(2); // 2: 已取消
        order.setCancelledAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    public boolean payOrder(Long orderId, String paymentMethod) {
        Order order = getById(orderId);
        if (order == null || order.getStatus() != 0) {
            return false;
        }

        order.setStatus(1); // 1: 已支付
        order.setPaymentMethod(paymentMethod);
        order.setPaidAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    public Order getOrderDetail(Long orderId) {
        return getById(orderId);
    }

    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    @Override
    public void cancelUnpaidOrders() {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getStatus, 0)
                   .le(Order::getCreatedAt, LocalDateTime.now().minusMinutes(30));
        
        List<Order> unpaidOrders = list(queryWrapper);
        for (Order order : unpaidOrders) {
            order.setStatus(2);
            order.setCancelledAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            updateById(order);
        }
    }

    private String generateOrderNo() {
        return "ORDER_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}