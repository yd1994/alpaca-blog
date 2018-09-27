package com.yd1994.alpacablog.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * categoty_article" 连接表
 *
 * @author yd
 */
@Entity
@IdClass(CategoryArticleId.class)
@Table(name = "alpaca_blog_category_article")
public class CategoryArticleDO implements Serializable {

    /**
     * ArticleDO ID
     */
    @Id
    private Long articleId;
    /**
     * CategoryDO ID
     */
    @Id
    private Long categoryId;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
