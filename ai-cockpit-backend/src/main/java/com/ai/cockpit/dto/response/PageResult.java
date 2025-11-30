package com.ai.cockpit.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应结果
 */
@Data
@NoArgsConstructor
public class PageResult<T> {
    
    /** 数据列表 */
    private List<T> items;
    
    /** 总记录数 */
    private Long total;
    
    /** 当前页码 */
    private Integer current;
    
    /** 每页大小 */
    private Integer size;
    
    /** 总页数 */
    private Integer pages;
    
    public PageResult(List<T> items, Long total, Integer current, Integer size) {
        this.items = items;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (int) Math.ceil((double) total / size);
    }
    
    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(List<T> items, Long total, Integer current, Integer size) {
        return new PageResult<>(items, total, current, size);
    }
    
    /**
     * 判断是否有上一页
     */
    public boolean hasPrevious() {
        return current > 1;
    }
    
    /**
     * 判断是否有下一页
     */
    public boolean hasNext() {
        return current < pages;
    }
}