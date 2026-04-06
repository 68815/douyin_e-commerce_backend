package com.example.commonmodule.client;

import com.example.commonmodule.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "order-service", path = "/order/rpc")
public interface OrderFeignClient {

    @GetMapping("/orderNo/{orderId}")
    String getOrderNoById(@PathVariable("orderId") Long orderId);

    @GetMapping("/status/{orderId}")
    Integer getOrderStatus(@PathVariable("orderId") Long orderId);

    @GetMapping("/amount/{orderId}")
    BigDecimal getOrderAmount(@PathVariable("orderId") Long orderId);

    @GetMapping("/exists")
    Boolean existsByOrderNo(@RequestParam("orderNo") String orderNo);
}
