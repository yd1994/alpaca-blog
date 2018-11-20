package com.yd1994.alpacablog.dto;

import com.yd1994.alpacablog.common.base.BaseDTO;
import com.yd1994.alpacablog.entity.ArticleTagDO;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

public class ArticleTag extends BaseDTO<ArticleTagDO> implements Serializable {


    private Long id;
    /**
     * 标签名称
     */
    private String name;
    /**
     * 标签描述
     */
    private String description;
    /**
     * 是否可用
     */
    private Boolean available;
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

    public ArticleTag() {
    }

    public ArticleTag(ArticleTagDO articleTagDO) {
        if (articleTagDO != null) {
            BeanUtils.copyProperties(articleTagDO, this);
            // 时间转换
            this.created = articleTagDO.getGmtCreated();
            this.modified = articleTagDO.getGmtModified();
        }
    }

    @Override
    public ArticleTagDO toEntity() {
        ArticleTagDO articleTagDO = new ArticleTagDO();
        BeanUtils.copyProperties(this, articleTagDO);
        articleTagDO.setGmtCreated(this.getCreated());
        articleTagDO.setGmtModified(this.getModified());
        return articleTagDO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "ArticleTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", version=" + version +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }
}
