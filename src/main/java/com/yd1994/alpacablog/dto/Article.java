package com.yd1994.alpacablog.dto;

import com.yd1994.alpacablog.common.base.BaseDTO;
import com.yd1994.alpacablog.entity.ArticleDO;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * dto 博文
 *
 * @author yd
 */
public class Article extends BaseDTO<ArticleDO> implements Serializable {

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
    private Date created;
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

    /**
     * 通过 ArticleDO 创建
     * @param articleDO
     */
    public Article(ArticleDO articleDO) {
        if (articleDO != null) {
            BeanUtils.copyProperties(articleDO, this);
            // 时间转换
            this.created = articleDO.getGmtCreated();
            this.modified = articleDO.getGmtModified();
            // 分类转换
            this.category = new Category(articleDO.getCategoryDO());
        }
    }

    public Article(Long id, String title, String content, String summary, Long traffic, Boolean top, Long version, Date created, Date modified, Category category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.traffic = traffic;
        this.top = top;
        this.version = version;
        this.created = created;
        this.modified = modified;
        this.category = category;
    }

    /**
     * 转换为 ArticleDO
     * @return
     */
    @Override
    public ArticleDO toEntity() {
        ArticleDO articleDO = new ArticleDO();
        BeanUtils.copyProperties(this, articleDO);
        articleDO.setGmtCreated(this.getCeated());
        articleDO.setGmtModified(this.getModified());
        if (this.getCategory() != null) {
            articleDO.setCategoryDO(this.getCategory().toEntity());
        }
        return articleDO;
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
        return created;
    }

    public void setCeated(Date created) {
        this.created = created;
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
                ", created=" + created +
                ", modified=" + modified +
                ", category=" + category +
                '}';
    }
}
