package app.service;

import java.math.BigDecimal;

/**
 * 结算服务接口
 */
public interface ISettlementService {

    /**
     * 订单结算
     */
    boolean settleOrder(Long orderId);

    /**
     * 计算订单总金额
     */
    BigDecimal calculateOrderTotal(Long orderId);

    /**
     * 验证库存
     */
    boolean validateStock(Long orderId);

    /**
     * 扣减库存
     */
    boolean deductStock(Long orderId);

    /**
     * 恢复库存（订单取消时）
     */
    boolean restoreStock(Long orderId);
}