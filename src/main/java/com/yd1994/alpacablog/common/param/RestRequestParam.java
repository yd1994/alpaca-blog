package com.yd1994.alpacablog.common.param;

import com.yd1994.alpacablog.common.exception.RequestParamErrorException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * restful集合 过滤参数
 *
 * @author yd
 */
public class RestRequestParam implements Serializable {

    /**
     * 分页信息：页码
     */
    private Integer page = 1;
    /**
     * 分页信息：每页记录数
     */
    private Integer size = 10;
    /**
     * 以sortByAsc asc排序
     */
    private String sortByAsc;
    /**
     * sortByDesc desc排序
     */
    private String sortByDesc;
    /**
     * before的参数
     */
    private String beforeBy;
    /**
     * 在beforeBy定义参数的时间之前
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date before;
    /**
     * after的参数
     */
    private String afterBy;
    /**
     * 在afterBy定义参数的时间参数之后
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date after;
    /**
     * 预定义搜索内容
     */
    private String view;

    /**
     * 获取jpa分页信息
     * @return
     */
    public Pageable getPageable() {
        if (page <= 0) {
            throw new RequestParamErrorException("参数page不能小于1或者为空。");
        }
        if (size <= 0) {
            throw new RequestParamErrorException("参数size不能小于1或者为空。");
        }
        return new PageRequest(page - 1, size);
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

    public String getSortByAsc() {
        return sortByAsc;
    }

    public void setSortByAsc(String sortByAsc) {
        this.sortByAsc = sortByAsc;
    }

    public String getSortByDesc() {
        return sortByDesc;
    }

    public void setSortByDesc(String sortByDesc) {
        this.sortByDesc = sortByDesc;
    }

    public String getBeforeBy() {
        return beforeBy;
    }

    public void setBeforeBy(String beforeBy) {
        this.beforeBy = beforeBy;
    }

    public Date getBefore() {
        return before;
    }

    public void setBefore(Date before) {
        this.before = before;
    }

    public String getAfterBy() {
        return afterBy;
    }

    public void setAfterBy(String afterBy) {
        this.afterBy = afterBy;
    }

    public Date getAfter() {
        return after;
    }

    public void setAfter(Date after) {
        this.after = after;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "RestRequestParam{" +
                "page=" + page +
                ", size=" + size +
                ", sortByAsc='" + sortByAsc + '\'' +
                ", sortByDesc='" + sortByDesc + '\'' +
                ", beforeBy='" + beforeBy + '\'' +
                ", before=" + before +
                ", afterBy='" + afterBy + '\'' +
                ", after=" + after +
                ", view='" + view + '\'' +
                '}';
    }
}
