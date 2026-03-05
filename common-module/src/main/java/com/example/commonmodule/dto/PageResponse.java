package com.example.commonmodule.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分页响应DTO
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
@Data
public class PageResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    private List<T> records;
    private Long total;
    private Integer current;
    private Integer size;
    private Integer pages;
    
    public static <T> PageResponse<T> of(List<T> records, Long total, Integer current, Integer size) {
        PageResponse<T> response = new PageResponse<>();
        response.setRecords(records);
        response.setTotal(total);
        response.setCurrent(current);
        response.setSize(size);
        response.setPages((int) Math.ceil((double) total / size));
        return response;
    }
}