package com.example.payment_service.controller;

import com.example.commonmodule.dto.Result;
import com.example.payment_service.VO.OrderPaymentVO;
import com.example.payment_service.VO.PaymentResultVO;
import com.example.payment_service.annotation.RateLimit;
import com.example.payment_service.request.CartCheckoutRequest;
import com.example.payment_service.request.DirectPurchaseRequest;
import com.example.payment_service.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final IPaymentService paymentService;

    @Value("${payment.callback.ip-whitelist:}")
    private String callbackIpWhitelist;

    @Autowired
    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RateLimit(value = 100, timeWindow = 1)
    @PostMapping("/direct-purchase")
    public Result<PaymentResultVO> directPurchase(@RequestBody DirectPurchaseRequest request) {
        try {
            PaymentResultVO result = paymentService.processDirectPurchase(request);
            if (Boolean.TRUE.equals(result.getSuccess())) {
                return Result.success(result.getMessage(), result);
            }
            return Result.error(result.getMessage());
        } catch (Exception e) {
            log.error("直接购买支付处理失败", e);
            return Result.error("支付处理失败，请稍后重试");
        }
    }

    @RateLimit(value = 100, timeWindow = 1)
    @PostMapping("/cart-checkout")
    public Result<PaymentResultVO> cartCheckout(@RequestBody CartCheckoutRequest request) {
        try {
            PaymentResultVO result = paymentService.processCartCheckout(request);
            if (Boolean.TRUE.equals(result.getSuccess())) {
                return Result.success(result.getMessage(), result);
            }
            return Result.error(result.getMessage());
        } catch (Exception e) {
            log.error("购物车结算支付处理失败", e);
            return Result.error("支付处理失败，请稍后重试");
        }
    }

    @RateLimit(value = 200, timeWindow = 1)
    @GetMapping("/detail/{paymentNo}")
    public Result<OrderPaymentVO> getOrderPaymentDetail(@PathVariable String paymentNo) {
        try {
            OrderPaymentVO paymentVO = paymentService.getOrderPaymentDetail(paymentNo);
            if (paymentVO != null) {
                return Result.success("查询成功", paymentVO);
            }
            return Result.error(404, "支付单不存在");
        } catch (Exception e) {
            log.error("获取订单支付详情失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    @RateLimit(value = 200, timeWindow = 1)
    @GetMapping("/status/{paymentNo}")
    public Result<PaymentResultVO> getPaymentStatus(@PathVariable String paymentNo) {
        try {
            PaymentResultVO result = paymentService.getPaymentStatus(paymentNo);
            return Result.success(result.getMessage(), result);
        } catch (Exception e) {
            log.error("查询支付状态失败", e);
            return Result.error("查询失败，请稍后重试");
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<String> handlePaymentCallback(
            @RequestBody String callbackData,
            HttpServletRequest request) {

        String clientIp = getClientIp(request);

        if (!isIpAllowed(clientIp)) {
            log.warn("非法回调IP: {}", clientIp);
            return ResponseEntity.status(403).body("FORBIDDEN");
        }

        try {
            boolean success = paymentService.handlePaymentCallback(callbackData);
            if (success) {
                return ResponseEntity.ok("SUCCESS");
            }
            return ResponseEntity.badRequest().body("FAILED");
        } catch (Exception e) {
            log.error("处理支付回调失败", e);
            return ResponseEntity.internalServerError().body("ERROR");
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private boolean isIpAllowed(String clientIp) {
        if (callbackIpWhitelist == null || callbackIpWhitelist.isEmpty()) {
            return true;
        }
        List<String> allowedIps = Arrays.asList(callbackIpWhitelist.split(","));
        return allowedIps.contains(clientIp);
    }
}
