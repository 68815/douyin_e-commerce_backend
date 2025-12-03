package com.example.merchandise_service.service.Impl;

import com.example.merchandise_service.entity.Product;
import com.example.merchandise_service.mapper.ProductMapper;
import com.example.merchandise_service.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
