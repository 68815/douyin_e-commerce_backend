package app.service.impl;

import app.entity.Order;
import app.service.IAIService;
import app.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Elasticsearch实现的AI服务（智能搜索升级版）
 */
@Service
public class AIServiceElasticsearchImpl implements IAIService {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<Map<String, Object>> queryOrders(String query) {
        // 使用Elasticsearch进行智能搜索
        Criteria criteria = new Criteria("orderNo").fuzzy(query).or("shippingAddress").fuzzy(query);
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        
        SearchHits<Order> searchHits = elasticsearchOperations.search(criteriaQuery, Order.class);
        
        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(order -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("orderNo", order.getOrderNo());
                    result.put("status", order.getStatus());
                    result.put("totalAmount", order.getTotalAmount());
                    result.put("createdAt", order.getCreatedAt());
                    result.put("score", searchHits.getMaxScore()); // 搜索相关性分数
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> simulateOrder(String productInfo, Integer quantity) {
        // 保持原有实现
        return new AIServiceImpl().simulateOrder(productInfo, quantity);
    }

    @Override
    public List<Map<String, Object>> recommendProducts(Long userId) {
        // 保持原有实现
        return new AIServiceImpl().recommendProducts(userId);
    }

    @Override
    public String chatWithAI(String message) {
        // 保持原有实现
        return new AIServiceImpl().chatWithAI(message);
    }

    @Override
    public Map<String, Object> analyzeOrderPatterns(Long userId) {
        // 保持原有实现
        return new AIServiceImpl().analyzeOrderPatterns(userId);
    }
}