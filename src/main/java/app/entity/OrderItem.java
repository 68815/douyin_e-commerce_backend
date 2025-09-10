package app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单项实体类
 */
@Data
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subtotal;

    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + id +
            ", orderId=" + orderId +
            ", productId=" + productId +
            ", quantity=" + quantity +
            ", price=" + price +
            ", subtotal=" + subtotal +
            '}';
    }
}