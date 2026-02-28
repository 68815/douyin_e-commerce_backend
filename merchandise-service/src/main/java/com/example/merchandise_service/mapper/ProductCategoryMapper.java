package com.example.merchandise_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.merchandise_service.DO.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品分类Mapper接口
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

}