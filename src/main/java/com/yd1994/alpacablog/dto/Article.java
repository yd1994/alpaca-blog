package com.yd1994.alpacablog.dto;

import com.yd1994.alpacablog.entity.ArticleDO;

import java.io.Serializable;
import java.util.Date;

/**
 * dto 博文
 *
 * @author yd
 */
public class Article implements Serializable {

    /**
     * ID
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 简介
     */
    private String summary;
    /**
     * 点击量
     */
    private Long traffic;
    /**
     * 是否置顶
     */
    private Boolean top;
    /**
     * 乐观锁
     */
    private Long version;
    /**
     * 创建日期
     */
    private Date ceated;
    /**
     * 最后修改日期
     */
    private Date modified;
    /**
     * 分类
     */
    private Category category;

    public Article() {
    }

    public Article(ArticleDO articleDO) {
        this(articleDO.getId(),
                articleDO.getTitle(),
                articleDO.getContent(),
                articleDO.getSummary(),
                articleDO.getTraffic(),
                articleDO.getTop(),
                articleDO.getVersion(),
                articleDO.getGmtCreated(),
                articleDO.getGmtModified(),
                new Category(articleDO.getCategoryDO()));
    }

    public Article(Long id, String title, String content, String summary, Long traffic, Boolean top, Long version, Date ceated, Date modified, Category category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.traffic = traffic;
        this.top = top;
        this.version = version;
        this.ceated = ceated;
        this.modified = modified;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getTraffic() {
        return traffic;
    }

    public void setTraffic(Long traffic) {
        this.traffic = traffic;
    }

    public Boolean getTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCeated() {
        return ceated;
    }

    public void setCeated(Date ceated) {
        this.ceated = ceated;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", traffic=" + traffic +
                ", top=" + top +
                ", version=" + version +
                ", ceated=" + ceated +
                ", modified=" + modified +
                ", category=" + category +
                '}';
    }
}
