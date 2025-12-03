package com.example.user_service.mapper;

import com.example.user_service.entity.VerificationCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 验证码Mapper接口
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@Mapper
public interface VerificationCodeMapper extends BaseMapper<VerificationCode> {

}