package com.example.payment_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.payment_service.entity.RefundRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 退款记录Mapper接口
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-28
 */
@Mapper
public interface RefundRecordMapper extends BaseMapper<RefundRecord> {

}