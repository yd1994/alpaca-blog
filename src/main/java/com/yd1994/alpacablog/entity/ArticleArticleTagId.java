package com.yd1994.alpacablog.entity;

import java.io.Serializable;

/**
 * @author yd
 */
public class ArticleArticleTagId implements Serializable {

    private Long articleId;

    private Long articleTagId;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getArticleTagId() {
        return articleTagId;
    }

    public void setArticleTagId(Long articleTagId) {
        this.articleTagId = articleTagId;
    }
}
