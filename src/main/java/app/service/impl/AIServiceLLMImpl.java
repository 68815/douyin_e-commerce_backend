package app.service.impl;

import app.entity.Order;
import app.entity.Product;
import app.service.IAIService;
import app.service.IOrderService;
import app.service.IProductService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 大语言模型实现的AI服务（真正的AI客服升级版）
 */
@Service
public class AIServiceLLMImpl implements IAIService {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ChatClient chatClient;

    @Value("${spring.ai.provider:openai}")
    private String aiProvider;

    @Override
    public List<Map<String, Object>> queryOrders(String query) {
        // 保持原有实现
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
        // 保持原有实现
        Map<String, Object> result = new HashMap<>();
        
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
        // 保持原有实现
        List<Product> products = productService.list();
        
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
        // 使用大语言模型实现智能客服
        String systemPrompt = """
        你是一个电商平台的AI客服助手，专门帮助用户处理订单查询、支付问题、商品推荐、退货流程、物流跟踪等问题。
        
        平台功能：
        - 订单查询：可以按订单号、收货地址搜索
        - 支付支持：支付宝、微信支付、银行卡
        - 退货政策：7天无理由退货
        - 物流合作：顺丰、圆通、中通
        
        请用友好、专业、有帮助的语气回答用户问题。如果无法确定答案，请引导用户提供更多信息。
        """;

        ChatResponse response = chatClient.prompt()
                .system(systemPrompt)
                .user(new UserMessage(message))
                .call()
                .chatResponse();

        return response.getResult().getOutput().getContent();
    }

    @Override
    public Map<String, Object> analyzeOrderPatterns(Long userId) {
        // 保持原有实现
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