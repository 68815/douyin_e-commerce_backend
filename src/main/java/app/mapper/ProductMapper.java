package app.mapper;

import app.entity.Product;
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
public interface ProductMapper extends BaseMapper<Product> {

}
