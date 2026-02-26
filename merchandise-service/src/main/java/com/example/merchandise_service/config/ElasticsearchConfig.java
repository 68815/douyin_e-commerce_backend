package com.example.merchandise_service.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * <p>
 * Elasticsearch 配置类
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-26
 */
@Configuration
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
    
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")  // ES服务器地址
                // .withBasicAuth("username", "password")  // 如需要认证
                .build();
        
        return RestClients.create(clientConfiguration).rest();
    }
}