package app.controller;

import app.service.IPaymentService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 支付控制器
 */
@RestController
@RequestMapping("/payment")
@SaCheckLogin
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    /**
     * 发起支付
     */
    @PostMapping("/{orderId}")
    public Map<String, Object> initiatePayment(@PathVariable Long orderId, 
                                               @RequestParam String paymentMethod) {
        boolean success = paymentService.initiatePayment(orderId, paymentMethod);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "支付发起成功" : "支付发起失败");
        return result;
    }

    /**
     * 取消支付
     */
    @PostMapping("/{orderId}/cancel")
    public Map<String, Object> cancelPayment(@PathVariable Long orderId) {
        boolean success = paymentService.cancelPayment(orderId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "支付取消成功" : "支付取消失败");
        return result;
    }

    /**
     * 查询支付状态
     */
    @GetMapping("/{orderId}/status")
    public Map<String, Object> getPaymentStatus(@PathVariable Long orderId) {
        String status = paymentService.getPaymentStatus(orderId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("status", status);
        return result;
    }

    /**
     * 处理支付回调
     */
    @PostMapping("/callback")
    public Map<String, Object> handlePaymentCallback(@RequestParam String paymentId, 
                                                    @RequestParam String status) {
        boolean success = paymentService.handlePaymentCallback(paymentId, status);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "回调处理成功" : "回调处理失败");
        return result;
    }
}