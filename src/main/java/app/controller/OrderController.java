package app.controller;

import app.entity.Order;
import app.service.IOrderService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
@SaCheckLogin
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public Map<String, Object> createOrder(@RequestBody List<Long> cartIds) {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        Order order = orderService.createOrder(userId, cartIds);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", order != null);
        result.put("order", order);
        result.put("message", order != null ? "订单创建成功" : "订单创建失败");
        return result;
    }

    /**
     * 获取用户订单列表
     */
    @GetMapping
    public List<Order> getUserOrders() {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        return orderService.getUserOrders(userId);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{orderId}")
    public Order getOrderDetail(@PathVariable Long orderId) {
        return orderService.getOrderDetail(orderId);
    }

    /**
     * 取消订单
     */
    @PostMapping("/{orderId}/cancel")
    public Map<String, Object> cancelOrder(@PathVariable Long orderId) {
        boolean success = orderService.cancelOrder(orderId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "订单取消成功" : "订单取消失败");
        return result;
    }

    /**
     * 支付订单
     */
    @PostMapping("/{orderId}/pay")
    public Map<String, Object> payOrder(@PathVariable Long orderId, 
                                        @RequestParam String paymentMethod) {
        boolean success = orderService.payOrder(orderId, paymentMethod);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "支付成功" : "支付失败");
        return result;
    }
}