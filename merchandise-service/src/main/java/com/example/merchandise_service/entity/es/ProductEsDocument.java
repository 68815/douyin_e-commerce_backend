package com.example.merchandise_service.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品ES文档实体类
 * 用于Elasticsearch搜索引擎
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-26
 */
@Document(indexName = "products")
@Data
public class ProductEsDocument {
    
    @Id
    private Long productId;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String productName;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String productDescription;
    
    @Field(type = FieldType.Keyword)
    private String categoryName;
    
    @Field(type = FieldType.Double)
    private BigDecimal price;
    
    @Field(type = FieldType.Integer)
    private Integer stockQuantity;
    
    @Field(type = FieldType.Integer)
    private Integer salesCount;
    
    @Field(type = FieldType.Boolean)
    private Boolean isHot;
    
    @Field(type = FieldType.Boolean)
    private Boolean isNew;

    @Field(type = FieldType.Integer)
    private Integer status;
    
    @Field(type = FieldType.Date)
    private LocalDateTime createTime;

    @Field(type = FieldType.Date)
    private LocalDateTime updateTime;
    
    // Constructors
    public ProductEsDocument() {}
    
    public ProductEsDocument(Long productId, String productName, String productDescription, 
                           BigDecimal price, Integer stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "ProductEsDocument{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", salesCount=" + salesCount +
                ", isHot=" + isHot +
                ", isNew=" + isNew +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}