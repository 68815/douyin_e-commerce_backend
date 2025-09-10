package app.service.impl;

import app.entity.Order;
import app.entity.Product;
import app.service.IAIService;
import app.service.IOrderService;
import app.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI大模型服务实现类
 */
@Service
public class AIServiceImpl implements IAIService {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Override
    public List<Map<String, Object>> queryOrders(String query) {
        // 模拟AI订单查询
        List<Order> orders = orderService.list();
        
        return orders.stream()
                .filter(order -> order.getOrderNo().toLowerCase().contains(query.toLowerCase()) ||
                        order.getShippingAddress().toLowerCase().contains(query.toLowerCase()))
                .map(order -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("orderNo", order.getOrderNo());
                    result.put("status", order.getStatus());
                    result.put("totalAmount", order.getTotalAmount());
                    result.put("createdAt", order.getCreatedAt());
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> simulateOrder(String productInfo, Integer quantity) {
        // 模拟自动下单
        Map<String, Object> result = new HashMap<>();
        
        // 模拟商品匹配
        List<Product> products = productService.list();
        Optional<Product> matchedProduct = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(productInfo.toLowerCase()) ||
                        product.getDescription().toLowerCase().contains(productInfo.toLowerCase()))
                .findFirst();

        if (matchedProduct.isPresent()) {
            Product product = matchedProduct.get();
            result.put("success", true);
            result.put("productId", product.getId());
            result.put("productName", product.getName());
            result.put("quantity", quantity);
            result.put("totalPrice", product.getPrice().multiply(new BigDecimal(quantity)));
        } else {
            result.put("success", false);
            result.put("message", "未找到匹配的商品");
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> recommendProducts(Long userId) {
        // 模拟商品推荐
        List<Product> products = productService.list();
        
        // 简单推荐逻辑：随机推荐3个商品
        Collections.shuffle(products);
        return products.stream()
                .limit(3)
                .map(product -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", product.getId());
                    result.put("name", product.getName());
                    result.put("price", product.getPrice());
                    result.put("description", product.getDescription());
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public String chatWithAI(String message) {
        // 模拟智能客服
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("订单") || lowerMessage.contains("order")) {
            return "我可以帮您查询订单信息，请提供订单号或相关信息。";
        } else if (lowerMessage.contains("支付") || lowerMessage.contains("payment")) {
            return "支付相关问题，请提供订单号，我会帮您查询支付状态。";
        } else if (lowerMessage.contains("退货") || lowerMessage.contains("return")) {
            return "退货流程：1. 登录账户 2. 找到订单 3. 申请退货 4. 等待审核";
        } else if (lowerMessage.contains("物流") || lowerMessage.contains("shipping")) {
            return "物流查询需要订单号，我可以帮您跟踪包裹状态。";
        } else {
            return "您好！我是电商AI助手，可以帮您处理订单查询、支付问题、商品推荐等。请告诉我您需要什么帮助？";
        }
    }

    @Override
    public Map<String, Object> analyzeOrderPatterns(Long userId) {
        // 模拟订单分析
        Map<String, Object> result = new HashMap<>();
        
        List<Order> userOrders = orderService.getUserOrders(userId);
        
        result.put("totalOrders", userOrders.size());
        result.put("totalSpent", userOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        result.put("averageOrderValue", userOrders.isEmpty() ? BigDecimal.ZERO :
                ((BigDecimal) result.get("totalSpent")).divide(new BigDecimal(userOrders.size()), 2));
        
        return result;
    }
}