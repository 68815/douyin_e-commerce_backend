package com.example.payment_service.mapper;

import com.example.payment_service.entity.Payment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

}