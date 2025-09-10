package app.service.impl;

import app.entity.Order;
import app.entity.OrderItem;
import app.entity.Product;
import app.mapper.OrderItemMapper;
import app.service.IOrderService;
import app.service.IProductService;
import app.service.ISettlementService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算服务实现类
 */
@Service
public class SettlementServiceImpl implements ISettlementService {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public boolean settleOrder(Long orderId) {
        Order order = orderService.getOrderDetail(orderId);
        if (order == null || order.getStatus() != 1) { // 1: 已支付
            return false;
        }

        // 验证库存
        if (!validateStock(orderId)) {
            return false;
        }

        // 扣减库存
        if (!deductStock(orderId)) {
            return false;
        }

        // 更新订单状态为已结算（3）
        order.setStatus(3);
        order.setUpdatedAt(java.time.LocalDateTime.now());
        return orderService.updateById(order);
    }

    @Override
    public BigDecimal calculateOrderTotal(Long orderId) {
        Order order = orderService.getOrderDetail(orderId);
        return order != null ? order.getTotalAmount() : BigDecimal.ZERO;
    }

    @Override
    public boolean validateStock(Long orderId) {
        List<OrderItem> orderItems = orderItemMapper.selectList(
            new QueryWrapper<OrderItem>().eq("order_id", orderId)
        );
        
        for (OrderItem item : orderItems) {
            Product product = productService.getById(item.getProductId());
            if (product == null || product.getStock() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deductStock(Long orderId) {
        List<OrderItem> orderItems = orderItemMapper.selectList(
            new QueryWrapper<OrderItem>().eq("order_id", orderId)
        );
        
        for (OrderItem item : orderItems) {
            Product product = productService.getById(item.getProductId());
            if (product != null) {
                product.setStock(product.getStock() - item.getQuantity());
                if (!productService.updateById(product)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean restoreStock(Long orderId) {
        List<OrderItem> orderItems = orderItemMapper.selectList(
            new QueryWrapper<OrderItem>().eq("order_id", orderId)
        );
        
        for (OrderItem item : orderItems) {
            Product product = productService.getById(item.getProductId());
            if (product != null) {
                product.setStock(product.getStock() + item.getQuantity());
                if (!productService.updateById(product)) {
                    return false;
                }
            }
        }
        return true;
    }
}