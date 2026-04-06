package com.example.order_service.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.commonmodule.dto.Result;
import com.example.order_service.DO.Order;
import com.example.order_service.DO.OrderItem;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("创建订单，用户ID: {}, 请求: {}", userId, request);

        if (request.getItems() == null || request.getItems().isEmpty()) {
            return Result.error("购物车不能为空");
        }
        if (request.getShippingAddress() == null || request.getShippingAddress().isEmpty()) {
            return Result.error("收货地址不能为空");
        }

        Order order = orderService.createOrder(userId, request);
        if (order != null) {
            return Result.success("订单创建成功", order);
        }
        return Result.error("订单创建失败");
    }

    @GetMapping("/list")
    public Result<List<Order>> getUserOrders() {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("获取用户订单列表，用户ID: {}", userId);
        List<Order> orders = orderService.getUserOrders(userId);
        return Result.success(orders);
    }

    @GetMapping("/{orderId}")
    public Result<Order> getOrderDetail(@PathVariable Long orderId) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("获取订单详情，用户ID: {}, 订单ID: {}", userId, orderId);
        Order order = orderService.getOrderDetail(orderId, userId);
        if (order != null) {
            return Result.success(order);
        }
        return Result.error(404, "订单不存在");
    }

    @GetMapping("/{orderId}/items")
    public Result<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("获取订单商品列表，用户ID: {}, 订单ID: {}", userId, orderId);
        List<OrderItem> items = orderService.getOrderItems(orderId, userId);
        return Result.success(items);
    }

    @PutMapping("/{orderId}/cancel")
    public Result<Boolean> cancelOrder(@PathVariable Long orderId) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("取消订单，用户ID: {}, 订单ID: {}", userId, orderId);
        boolean success = orderService.cancelOrder(orderId, userId);
        if (success) {
            return Result.success("订单取消成功", true);
        }
        return Result.error("订单取消失败");
    }

    @PutMapping("/{orderId}/pay")
    public Result<Boolean> payOrder(@PathVariable Long orderId, @RequestParam String paymentMethod) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("支付订单，用户ID: {}, 订单ID: {}, 支付方式: {}", userId, orderId, paymentMethod);
        boolean success = orderService.payOrder(orderId, userId, paymentMethod);
        if (success) {
            return Result.success("支付成功", true);
        }
        return Result.error("支付失败");
    }

    @PutMapping("/{orderId}/confirm")
    public Result<Boolean> confirmOrder(@PathVariable Long orderId) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("确认收货，用户ID: {}, 订单ID: {}", userId, orderId);
        boolean success = orderService.confirmOrder(orderId, userId);
        if (success) {
            return Result.success("确认收货成功", true);
        }
        return Result.error("确认收货失败");
    }

    @GetMapping("/rpc/orderNo/{orderId}")
    public String getOrderNoById(@PathVariable Long orderId) {
        Order order = orderService.getById(orderId);
        return order != null ? order.getOrderNo() : null;
    }

    @GetMapping("/rpc/status/{orderId}")
    public Integer getOrderStatus(@PathVariable Long orderId) {
        Order order = orderService.getById(orderId);
        return order != null ? order.getStatus() : null;
    }

    @GetMapping("/rpc/amount/{orderId}")
    public BigDecimal getOrderAmount(@PathVariable Long orderId) {
        Order order = orderService.getById(orderId);
        return order != null ? order.getTotalAmount() : null;
    }

    @GetMapping("/rpc/exists")
    public Boolean existsByOrderNo(@RequestParam String orderNo) {
        return orderService.existsByOrderNo(orderNo);
    }
}
