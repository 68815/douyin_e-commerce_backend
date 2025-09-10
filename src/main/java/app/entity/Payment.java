package app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付实体类
 */
@Data
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付状态
     * 0: 待支付, 1: 支付成功, 2: 支付失败, 3: 已取消
     */
    private Integer status;

    /**
     * 支付平台交易号
     */
    private String transactionId;

    /**
     * 支付创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 支付更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 支付完成时间
     */
    private LocalDateTime paidAt;

    /**
     * 支付取消时间
     */
    private LocalDateTime cancelledAt;

    /**
     * 支付回调数据
     */
    private String callbackData;

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                ", transactionId='" + transactionId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", paidAt=" + paidAt +
                ", cancelledAt=" + cancelledAt +
                ", callbackData='" + callbackData + '\'' +
                '}';
    }
}