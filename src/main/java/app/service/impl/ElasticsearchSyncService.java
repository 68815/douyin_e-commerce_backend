package app.service.impl;

import app.entity.Order;
import app.entity.OrderES;
import app.service.IOrderService;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Elasticsearch数据同步服务
 */
@Service
public class ElasticsearchSyncService {

    private final IOrderService orderService;
    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticsearchSyncService(IOrderService orderService, 
                                   ElasticsearchOperations elasticsearchOperations) {
        this.orderService = orderService;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * 全量同步订单数据到Elasticsearch
     */
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    public void fullSyncOrders() {
        List<Order> orders = orderService.list();
        List<OrderES> orderESList = orders.stream()
                .map(OrderES::new)
                .collect(Collectors.toList());
        
        elasticsearchOperations.save(orderESList);
    }

    /**
     * 增量同步（订单创建时调用）
     */
    public void syncOrder(Order order) {
        OrderES orderES = new OrderES(order);
        elasticsearchOperations.save(orderES);
    }

    /**
     * 增量同步（订单更新时调用）
     */
    public void updateOrder(Order order) {
        OrderES orderES = new OrderES(order);
        elasticsearchOperations.save(orderES);
    }

    /**
     * 删除同步（订单删除时调用）
     */
    public void deleteOrder(Long orderId) {
        elasticsearchOperations.delete(orderId.toString(), OrderES.class);
    }

    /**
     * 手动触发同步
     */
    public void triggerSync() {
        fullSyncOrders();
    }
}