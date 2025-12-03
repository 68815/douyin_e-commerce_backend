package com.example.user_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.example.user_service.entity.User;
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
