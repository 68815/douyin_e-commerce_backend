package app.controller;

import app.service.IAIService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AI大模型控制器
 */
@RestController
@RequestMapping("/ai")
@SaCheckLogin
public class AIController {

    @Autowired
    private IAIService aiService;

    /**
     * 订单查询
     */
    @GetMapping("/orders")
    public List<Map<String, Object>> queryOrders(@RequestParam String query) {
        return aiService.queryOrders(query);
    }

    /**
     * 模拟自动下单
     */
    @PostMapping("/simulate-order")
    public Map<String, Object> simulateOrder(@RequestParam String productInfo, 
                                           @RequestParam Integer quantity) {
        return aiService.simulateOrder(productInfo, quantity);
    }

    /**
     * 商品推荐
     */
    @GetMapping("/recommend")
    public List<Map<String, Object>> recommendProducts() {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        return aiService.recommendProducts(userId);
    }

    /**
     * 智能客服
     */
    @PostMapping("/chat")
    public Map<String, Object> chatWithAI(@RequestParam String message) {
        String response = aiService.chatWithAI(message);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("response", response);
        return result;
    }

    /**
     * 订单分析
     */
    @GetMapping("/analysis")
    public Map<String, Object> analyzeOrderPatterns() {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        return aiService.analyzeOrderPatterns(userId);
    }
}