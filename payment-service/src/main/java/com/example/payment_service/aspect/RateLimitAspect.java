package com.example.payment_service.aspect;

import com.example.payment_service.annotation.RateLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RateLimitAspect(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(com.example.payment_service.annotation.RateLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        String key = "rate:limit:" + method.getDeclaringClass().getName() + ":" + method.getName();

        int maxRequests = rateLimit.value();
        int timeWindow = rateLimit.timeWindow();

        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            redisTemplate.expire(key, timeWindow, TimeUnit.SECONDS);
        }

        if (count != null && count > maxRequests) {
            log.warn("请求过于频繁: {} - 当前请求数: {}", key, count);
            throw new RuntimeException("请求过于频繁，请稍后重试");
        }

        return joinPoint.proceed();
    }
}
