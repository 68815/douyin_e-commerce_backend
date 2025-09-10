package app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long userId;

    private BigDecimal totalAmount;

    private Integer status;

    private String shippingAddress;

    private String paymentMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime paidAt;

    private LocalDateTime cancelledAt;

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", orderNo='" + orderNo + '\'' +
            ", userId=" + userId +
            ", totalAmount=" + totalAmount +
            ", status=" + status +
            ", shippingAddress='" + shippingAddress + '\'' +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", paidAt=" + paidAt +
            ", cancelledAt=" + cancelledAt +
            '}';
    }
}