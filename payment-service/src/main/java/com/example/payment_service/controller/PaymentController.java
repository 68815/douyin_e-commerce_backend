package com.example.payment_service.controller;

import com.example.payment_service.VO.OrderPaymentVO;
import com.example.payment_service.VO.PaymentResultVO;
import com.example.payment_service.request.CartCheckoutRequest;
import com.example.payment_service.request.DirectPurchaseRequest;
import com.example.payment_service.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 支付控制器
 */
@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final IPaymentService paymentService;
    @Autowired
    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 直接购买商品支付
     */
    @PostMapping("/direct-purchase")
    public ResponseEntity<PaymentResultVO> directPurchase(@RequestBody DirectPurchaseRequest request) {
        try {
            PaymentResultVO result = paymentService.processDirectPurchase(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("直接购买支付处理失败", e);
            return ResponseEntity.internalServerError()
                    .body(PaymentResultVO.failed("支付处理失败: " + e.getMessage()));
        }
    }

    /**
     * 购物车结算支付
     */
    @PostMapping("/cart-checkout")
    public ResponseEntity<PaymentResultVO> cartCheckout(@RequestBody CartCheckoutRequest request) {
        try {
            PaymentResultVO result = paymentService.processCartCheckout(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("购物车结算支付处理失败", e);
            return ResponseEntity.internalServerError()
                    .body(PaymentResultVO.failed("支付处理失败: " + e.getMessage()));
        }
    }

    /**
     * 获取订单支付详情（通用接口）
     */
    @GetMapping("/detail/{paymentNo}")
    public ResponseEntity<OrderPaymentVO> getOrderPaymentDetail(@PathVariable String paymentNo) {
        try {
            OrderPaymentVO paymentVO = paymentService.getOrderPaymentDetail(paymentNo);
            if (paymentVO != null) {
                return ResponseEntity.ok(paymentVO);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("获取订单支付详情失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 查询支付状态
     */
    @GetMapping("/status/{paymentNo}")
    public ResponseEntity<PaymentResultVO> getPaymentStatus(@PathVariable String paymentNo) {
        try {
            PaymentResultVO result = paymentService.getPaymentStatus(paymentNo);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("查询支付状态失败", e);
            return ResponseEntity.internalServerError()
                    .body(PaymentResultVO.failed("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 处理支付回调
     */
    @PostMapping("/callback")
    public ResponseEntity<String> handlePaymentCallback(@RequestBody String callbackData) {
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
}