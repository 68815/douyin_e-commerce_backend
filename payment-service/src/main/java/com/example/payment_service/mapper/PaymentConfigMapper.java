package com.example.payment_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.payment_service.DO.PaymentConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 支付配置Mapper接口
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-28
 */
@Mapper
public interface PaymentConfigMapper extends BaseMapper<PaymentConfig> {

}