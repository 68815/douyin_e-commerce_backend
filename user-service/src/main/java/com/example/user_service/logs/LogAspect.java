package com.example.user_service.logs;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
/**
 * 日志切面类
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    /**
     * 定义切点，拦截所有controller包下的所有方法
     */
    @Pointcut("execution(* com.example.user_service.controller..*(..))")
    public void controllerLogPointCut() {}

    /**
     * 定义切点，拦截所有service包下的所有方法
     */
    @Pointcut("execution(* com.example.user_service.service..*(..))")
    public void serviceLogPointCut() {}


    /**
     * 环绕通知，处理日志记录逻辑
     */
    @Around("serviceLogPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        logMethodEntry(point);
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - beginTime;
        logMethodExit(point, time, result);
        return result;
    }

    @Before("serviceLogPointCut()")
    public void before(JoinPoint joinPoint) {

    }

    @After("serviceLogPointCut()")
    public void after(JoinPoint joinPoint) {

    }

    @AfterReturning(pointcut = "serviceLogPointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {

    }

    @AfterThrowing(pointcut = "serviceLogPointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        // 方法抛出异常时执行
        log.error("[AFTER_THROWING] 方法 {} 抛出异常 {}: {}", 
            joinPoint.getSignature().toShortString(), 
            e.getClass().getSimpleName(), 
            e.getMessage());
    }
    
    /**
     * 记录方法入口信息
     */
    private void logMethodEntry(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.toShortString();
        Object[] args = point.getArgs();

        StringBuilder argsInfo = new StringBuilder();
        if (args != null && args.length > 0) {
            argsInfo.append(" 参数: ");
            for (int i = 0; i < args.length; i++) {
                if (i > 0) argsInfo.append(", ");
                argsInfo.append("arg").append(i).append("=").append(args[i]);
            }
        }
        String targetClass = point.getTarget().getClass().getSimpleName();
        log.info("[METHOD_ENTRY] {}.{} 开始执行{}", targetClass, methodName, argsInfo);
    }
    
    /**
     * 记录方法出口信息
     */
    private void logMethodExit(ProceedingJoinPoint point, long executionTime, Object result) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.toShortString();
        String targetClass = point.getTarget().getClass().getSimpleName();
        log.info("[METHOD_EXIT] {}.{} 执行完成, 耗时: {}ms",
            targetClass, methodName, executionTime);
    }
}