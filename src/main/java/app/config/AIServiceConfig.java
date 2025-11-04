package app.config;

import app.service.IAIService;
import app.service.impl.AIServiceElasticsearchImpl;
import app.service.impl.AIServiceImpl;
import app.service.impl.AIServiceLLMImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * AI服务配置类
 */
@Configuration
public class AIServiceConfig {

    @Value("${ai.service.impl:default}")
    private String aiServiceImpl;

    @Value("${ai.search.impl:default}")
    private String aiSearchImpl;

    @Value("${ai.chat.impl:default}")
    private String aiChatImpl;

    /**
     * 根据配置选择AI服务实现
     */
    @Bean
    @Primary
    public IAIService aiService(AIServiceImpl defaultImpl,
                               AIServiceElasticsearchImpl elasticsearchImpl,
                               AIServiceLLMImpl llmImpl) {
        switch (aiServiceImpl.toLowerCase()) {
            case "elasticsearch":
                return elasticsearchImpl;
            case "llm":
                return llmImpl;
            case "hybrid":
                // 可以创建混合实现
                return createHybridService(elasticsearchImpl, llmImpl);
            default:
                return defaultImpl;
        }
    }

    /**
     * 创建混合AI服务（智能搜索 + AI客服）
     */
    private IAIService createHybridService(AIServiceElasticsearchImpl elasticsearchImpl,
                                          AIServiceLLMImpl llmImpl) {
        return new IAIService() {
            @Override
            public List<Map<String, Object>> queryOrders(String query) {
                // 使用Elasticsearch进行智能搜索
                return elasticsearchImpl.queryOrders(query);
            }

            @Override
            public Map<String, Object> simulateOrder(String productInfo, Integer quantity) {
                // 使用默认实现
                return elasticsearchImpl.simulateOrder(productInfo, quantity);
            }

            @Override
            public List<Map<String, Object>> recommendProducts(Long userId) {
                // 使用默认实现
                return elasticsearchImpl.recommendProducts(userId);
            }

            @Override
            public String chatWithAI(String message) {
                // 使用大语言模型实现AI客服
                return llmImpl.chatWithAI(message);
            }

            @Override
            public Map<String, Object> analyzeOrderPatterns(Long userId) {
                // 使用默认实现
                return elasticsearchImpl.analyzeOrderPatterns(userId);
            }
        };
    }

    /**
     * 单独的Elasticsearch搜索服务（可选）
     */
    @Bean
    public AIServiceElasticsearchImpl aiServiceElasticsearch() {
        return new AIServiceElasticsearchImpl();
    }

    /**
     * 单独的大语言模型服务（可选）
     */
    @Bean
    public AIServiceLLMImpl aiServiceLLM() {
        return new AIServiceLLMImpl();
    }
}