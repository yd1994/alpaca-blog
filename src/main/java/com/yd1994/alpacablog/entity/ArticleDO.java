package com.yd1994.alpacablog.entity;

import com.yd1994.alpacablog.dto.ArticleTag;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 博文
 *
 * @author yd
 */
@Entity
@Table(name = "alpaca_blog_article")
public class ArticleDO implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 标题
     */
    @Column
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
    private Long traffic = 0L;
    /**
     * 是否置顶
     */
    @Column(name = "is_top")
    private Boolean top = false;
    /**
     * 是否被删除
     */
    @Column(name = "is_delete")
    private Boolean delete = false;
    /**
     * 乐观锁
     */
    @Version
    private Long version = 0L;
    /**
     * 创建日期
     */
    @CreatedDate
    private Date gmtCreated;
    /**
     * 创建人
     */
    @CreatedBy
    private String createdBy;
    /**
     * 最后修改日期
     */
    @LastModifiedDate
    private Date gmtModified;
    /**
     * 最后修改人
     */
    @LastModifiedBy
    private String modifiedBy;
    /**
     * 分类
     */
    @OneToOne(targetEntity = CategoryDO.class)
    @JoinTable(name = "alpaca_blog_category_article",
            joinColumns = {@JoinColumn(name = "articleId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "categoryId", referencedColumnName = "id")})
    private CategoryDO categoryDO;

    /**
     * 标签
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "alpaca_blog_article_article_tag",
            joinColumns = {@JoinColumn(name = "articleId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "articleTagId", referencedColumnName = "id")})
    private List<ArticleTagDO> articleTagDOList;

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

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public CategoryDO getCategoryDO() {
        return categoryDO;
    }

    public void setCategoryDO(CategoryDO categoryDO) {
        this.categoryDO = categoryDO;
    }

    public List<ArticleTagDO> getArticleTagDOList() {
        return articleTagDOList;
    }

    public void setArticleTagDOList(List<ArticleTagDO> articleTagDOList) {
        this.articleTagDOList = articleTagDOList;
    }

    @Override
    public String toString() {
        return "ArticleDO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", traffic=" + traffic +
                ", top=" + top +
                ", delete=" + delete +
                ", version=" + version +
                ", gmtCreated=" + gmtCreated +
                ", createdBy='" + createdBy + '\'' +
                ", gmtModified=" + gmtModified +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", categoryDO=" + categoryDO +
                ", articleTagDOList=" + articleTagDOList +
                '}';
    }
}
