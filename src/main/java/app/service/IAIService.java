package app.service;

import java.util.List;
import java.util.Map;

/**
 * AI大模型服务接口
 */
public interface IAIService {

    /**
     * 订单查询
     */
    List<Map<String, Object>> queryOrders(String query);

    /**
     * 模拟自动下单
     */
    Map<String, Object> simulateOrder(String productInfo, Integer quantity);

    /**
     * 商品推荐
     */
    List<Map<String, Object>> recommendProducts(Long userId);

    /**
     * 智能客服
     */
    String chatWithAI(String message);

    /**
     * 订单分析
     */
    Map<String, Object> analyzeOrderPatterns(Long userId);
}