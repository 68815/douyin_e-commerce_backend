package com.example.payment_service.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.commonmodule.client.CartFeignClient;
import com.example.commonmodule.client.ProductFeignClient;
import com.example.commonmodule.dto.CartItemDetailDto;
import com.example.commonmodule.dto.ProductResponse;
import com.example.payment_service.DO.Payment;
import com.example.payment_service.DO.PaymentLog;
import com.example.payment_service.VO.OrderPaymentVO;
import com.example.payment_service.VO.PaymentResultVO;
import com.example.payment_service.config.PaymentConfig;
import com.example.payment_service.enums.PaymentStatusEnum;
import com.example.payment_service.mapper.PaymentLogMapper;
import com.example.payment_service.mapper.PaymentMapper;
import com.example.payment_service.request.CartCheckoutRequest;
import com.example.payment_service.request.DirectPurchaseRequest;
import com.example.payment_service.service.IPaymentService;
import com.example.payment_service.strategy.PaymentStrategy;
import com.example.payment_service.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentLogMapper paymentLogMapper;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final PaymentConfig paymentConfig;
    private final StringRedisTemplate redisTemplate;
    private final ProductFeignClient productFeignClient;
    private final CartFeignClient cartFeignClient;

    private static final String IDEMPOTENCY_KEY_PREFIX = "payment:idempotency:";
    private static final int IDEMPOTENCY_EXPIRE_SECONDS = 3600;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResultVO processDirectPurchase(DirectPurchaseRequest request) {
        String paymentNo = generatePaymentNo();
        String idempotencyKey = IDEMPOTENCY_KEY_PREFIX + paymentNo;

        if (!checkIdempotency(idempotencyKey)) {
            return PaymentResultVO.failed("重复请求，请稍后重试");
        }

        try {
            Long amount = calculateProductPrice(request.getProductId(), request.getQuantity());

            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(request.getPaymentMethod());

            Map<String, String> paymentResult = strategy.createPaymentOrder(
                paymentNo,
                amount,
                "商品购买",
                request.getReturnUrl(),
                request.getNotifyUrl()
            );

            Payment payment = new Payment();
            payment.setPaymentNo(paymentNo);
            payment.setOrderId(request.getProductId());
            payment.setUserId(request.getUserId());
            payment.setAmount(new BigDecimal(amount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setPaymentStatus(PaymentStatusEnum.PENDING.getCode());
            payment.setGatewayResponse(paymentResult.toString());
            payment.setCreatedTime(LocalDateTime.now());
            payment.setExpiredTime(LocalDateTime.now().plusMinutes(30));

            paymentMapper.insert(payment);

            logPayment(paymentNo, "CREATE", "创建支付订单成功");

            return PaymentResultVO.success("支付订单创建成功", paymentResult);

        } catch (Exception e) {
            log.error("创建直接购买订单失败", e);
            return PaymentResultVO.failed("支付订单创建失败，请稍后重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResultVO processCartCheckout(CartCheckoutRequest request) {
        String paymentNo = generatePaymentNo();
        String cartItems = String.join(",", request.getCartItemIds().stream().map(String::valueOf).toList());
        String idempotencyKey = IDEMPOTENCY_KEY_PREFIX + paymentNo;

        if (!checkIdempotency(idempotencyKey)) {
            return PaymentResultVO.failed("重复请求，请稍后重试");
        }

        try {
            Long amount = calculateCartTotal(request.getUserId(), request.getCartItemIds());

            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(request.getPaymentMethod());

            Map<String, String> paymentResult = strategy.createPaymentOrder(
                paymentNo,
                amount,
                "购物车结算",
                request.getReturnUrl(),
                request.getNotifyUrl()
            );

            Payment payment = new Payment();
            payment.setPaymentNo(paymentNo);
            payment.setOrderId(request.getUserId());
            payment.setUserId(request.getUserId());
            payment.setAmount(new BigDecimal(amount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setPaymentStatus(PaymentStatusEnum.PENDING.getCode());
            payment.setGatewayResponse(paymentResult.toString());
            payment.setCreatedTime(LocalDateTime.now());
            payment.setExpiredTime(LocalDateTime.now().plusMinutes(30));

            paymentMapper.insert(payment);

            logPayment(paymentNo, "CREATE", "创建购物车支付订单成功");

            return PaymentResultVO.success("支付订单创建成功", paymentResult);

        } catch (Exception e) {
            log.error("创建购物车结算订单失败", e);
            return PaymentResultVO.failed("支付订单创建失败，请稍后重试");
        }
    }

    @Override
    public OrderPaymentVO getOrderPaymentDetail(String paymentNo) {
        Payment payment = paymentMapper.selectOne(
            new LambdaQueryWrapper<Payment>()
                .eq(Payment::getPaymentNo, paymentNo)
        );

        if (payment == null) {
            return null;
        }

        return convertToOrderPaymentVO(payment);
    }

    @Override
    public PaymentResultVO getPaymentStatus(String paymentNo) {
        Payment payment = paymentMapper.selectOne(
            new LambdaQueryWrapper<Payment>()
                .eq(Payment::getPaymentNo, paymentNo)
        );

        if (payment == null) {
            return PaymentResultVO.failed("支付单不存在");
        }

        return PaymentResultVO.success("查询成功", Map.of(
            "paymentNo", payment.getPaymentNo(),
            "status", PaymentStatusEnum.fromCode(payment.getPaymentStatus()).getDescription(),
            "amount", payment.getAmount().toString()
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentCallback(String callbackData) {
        try {
            Map<String, String> params = parseCallbackParams(callbackData);

            String paymentMethod = determinePaymentMethod(params);
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentMethod);

            if (!strategy.verifyCallback(params)) {
                log.warn("支付回调验签失败: {}", params);
                logPayment("unknown", "VERIFY_FAILED", "验签失败");
                return false;
            }

            String paymentNo = getPaymentNoFromCallback(params);
            Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                    .eq(Payment::getPaymentNo, paymentNo)
            );

            if (payment == null) {
                log.error("支付单不存在: {}", paymentNo);
                return false;
            }

            if (payment.getPaymentStatus() == PaymentStatusEnum.SUCCESS.getCode()) {
                log.info("支付单已处理: {}", paymentNo);
                return true;
            }

            String transactionId = strategy.getTransactionId(params);
            String status = strategy.getPaymentStatus(params);

            payment.setTransactionId(transactionId);
            payment.setPaymentStatus("SUCCESS".equals(status)
                ? PaymentStatusEnum.SUCCESS.getCode()
                : PaymentStatusEnum.FAILED.getCode());
            payment.setPaidTime(LocalDateTime.now());
            payment.setUpdatedTime(LocalDateTime.now());

            paymentMapper.updateById(payment);

            logPayment(paymentNo, "CALLBACK_SUCCESS", "支付回调处理成功, transactionId: " + transactionId);

            return true;

        } catch (Exception e) {
            log.error("处理支付回调失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPendingPayments() {
        LocalDateTime expiredTime = LocalDateTime.now();

        LambdaQueryWrapper<Payment> wrapper = new LambdaQueryWrapper<Payment>()
            .eq(Payment::getPaymentStatus, PaymentStatusEnum.PENDING.getCode())
            .lt(Payment::getExpiredTime, expiredTime);

        var pendingPayments = paymentMapper.selectList(wrapper);

        for (Payment payment : pendingPayments) {
            payment.setPaymentStatus(PaymentStatusEnum.FAILED.getCode());
            payment.setUpdatedTime(LocalDateTime.now());
            paymentMapper.updateById(payment);

            logPayment(payment.getPaymentNo(), "CANCEL", "超时未支付，已自动取消");
        }

        log.info("已取消 {} 个超时未支付的订单", pendingPayments.size());
    }

    private boolean checkIdempotency(@NotNull String key) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(
            key,
            UUID.randomUUID().toString(),
            IDEMPOTENCY_EXPIRE_SECONDS,
            TimeUnit.SECONDS
        );
        return Boolean.TRUE.equals(result);
    }

    private String generatePaymentNo() {
        return "PAY" + System.currentTimeMillis() + String.format("%04d", (int) (Math.random() * 10000));
    }

    private Long calculateProductPrice(Long productId, Integer quantity) {
        try {
            ProductResponse product = productFeignClient.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("商品不存在: " + productId);
            }
            if (product.getPrice() == null) {
                throw new RuntimeException("商品价格未设置: " + productId);
            }
            return product.getPrice().multiply(new BigDecimal(quantity))
                .multiply(new BigDecimal(100))
                .longValue();
        } catch (Exception e) {
            log.error("获取商品价格失败, productId: {}", productId, e);
            throw new RuntimeException("获取商品价格失败: " + e.getMessage());
        }
    }

    private Long calculateCartTotal(Long userId, java.util.List<Long> cartItemIds) {
        try {
            List<CartItemDetailDto> cartItems = cartFeignClient.getCartByUserId(userId);
            if (cartItems == null || cartItems.isEmpty()) {
                throw new RuntimeException("购物车为空");
            }

            Map<Long, CartItemDetailDto> cartMap = new HashMap<>();
            for (CartItemDetailDto item : cartItems) {
                cartMap.put(item.getProductId(), item);
            }

            long total = 0;
            for (Long productId : cartItemIds) {
                CartItemDetailDto item = cartMap.get(productId);
                if (item != null && item.getPrice() != null) {
                    int quantity = item.getQuantity() != null ? item.getQuantity() : 1;
                    total += item.getPrice().multiply(new BigDecimal(quantity))
                        .multiply(new BigDecimal(100))
                        .longValue();
                }
            }
            return total;
        } catch (Exception e) {
            log.error("获取购物车商品价格失败, userId: {}, cartItemIds: {}", userId, cartItemIds, e);
            throw new RuntimeException("获取购物车商品价格失败: " + e.getMessage());
        }
    }

    private void logPayment(String paymentNo, String action, String message) {
        PaymentLog paymentLog = new PaymentLog();
        paymentLog.setOperation(action);
        paymentLog.setRequestData(message);
        paymentLog.setCreatedTime(LocalDateTime.now());
        paymentLogMapper.insert(paymentLog);
    }

    private OrderPaymentVO convertToOrderPaymentVO(Payment payment) {
        OrderPaymentVO vo = new OrderPaymentVO();
        vo.setPaymentNo(payment.getPaymentNo());
        vo.setOrderNo(String.valueOf(payment.getOrderId()));
        vo.setUserId(payment.getUserId());
        vo.setTotalAmount(payment.getAmount());
        vo.setPaymentStatus(payment.getPaymentStatus());
        vo.setPaymentMethod(payment.getPaymentMethod());
        vo.setTransactionId(payment.getTransactionId());
        vo.setCreatedTime(payment.getCreatedTime());
        vo.setPaidTime(payment.getPaidTime());
        vo.setExpiredTime(payment.getExpiredTime());
        return vo;
    }

    private Map<String, String> parseCallbackParams(String callbackData) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = callbackData.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                params.put(kv[0], kv[1]);
            }
        }
        return params;
    }

    private String determinePaymentMethod(Map<String, String> params) {
        if (params.containsKey("trade_no")) {
            return "ALIPAY";
        } else if (params.containsKey("transaction_id")) {
            return "WECHAT";
        }
        return "ALIPAY";
    }

    private String getPaymentNoFromCallback(Map<String, String> params) {
        return params.get("out_trade_no");
    }
}
