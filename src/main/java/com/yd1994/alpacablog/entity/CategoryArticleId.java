package com.yd1994.alpacablog.entity;

import java.io.Serializable;

/**
 * category_article ID
 *
 * @author yd
 */
public class CategoryArticleId implements Serializable {

    private Long articleId;

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
