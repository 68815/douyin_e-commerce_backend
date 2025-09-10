package app.controller;

import app.service.ISettlementService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 结算控制器
 */
@RestController
@RequestMapping("/settlement")
@SaCheckLogin
public class SettlementController {

    @Autowired
    private ISettlementService settlementService;

    /**
     * 订单结算
     */
    @PostMapping("/{orderId}")
    public Map<String, Object> settleOrder(@PathVariable Long orderId) {
        boolean success = settlementService.settleOrder(orderId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "结算成功" : "结算失败");
        return result;
    }

    /**
     * 计算订单总金额
     */
    @GetMapping("/{orderId}/total")
    public Map<String, Object> calculateOrderTotal(@PathVariable Long orderId) {
        BigDecimal total = settlementService.calculateOrderTotal(orderId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("totalAmount", total);
        return result;
    }

    /**
     * 验证库存
     */
    @GetMapping("/{orderId}/validate-stock")
    public Map<String, Object> validateStock(@PathVariable Long orderId) {
        boolean valid = settlementService.validateStock(orderId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("valid", valid);
        result.put("message", valid ? "库存充足" : "库存不足");
        return result;
    }
}