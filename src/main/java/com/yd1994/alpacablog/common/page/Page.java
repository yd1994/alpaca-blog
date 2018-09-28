package com.yd1994.alpacablog.common.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

/**
 * 分页信息
 *
 * @author yd
 */
public class Page implements Serializable {

    /**
     * 分页信息：页码
     */
    private Integer page = 1;
    /**
     * 分页信息：每页记录数
     */
    private Integer size = 10;

    /**
     * 转换为 Jpa分页 Pageable类
     * @return
     */
    public Pageable toPageable() {
        return new PageRequest(this.page - 1, this.size);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Page{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}
