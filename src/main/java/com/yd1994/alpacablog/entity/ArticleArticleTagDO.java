package com.yd1994.alpacablog.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author yd
 */
@Entity
@Table(name = "alpaca_blog_article_article_tag")
@IdClass(ArticleArticleTagId.class)
public class ArticleArticleTagDO implements Serializable {

    @Id
    private Long articleId;

    @Id
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

    @Override
    public String toString() {
        return "ArticleArticleTagDO{" +
                "articleId=" + articleId +
                ", articleTagId=" + articleTagId +
                '}';
    }
}
