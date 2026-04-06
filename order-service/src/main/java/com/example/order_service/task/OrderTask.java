package com.example.order_service.task;

import com.example.order_service.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderTask {

    @Autowired
    private IOrderService orderService;

    @Scheduled(cron = "0 */10 * * * ?")
    public void cancelUnpaidOrders() {
        log.info("定时任务：开始检查并取消未支付订单");
        try {
            orderService.cancelUnpaidOrders();
        } catch (Exception e) {
            log.error("定时任务：取消未支付订单失败", e);
        }
    }
}
