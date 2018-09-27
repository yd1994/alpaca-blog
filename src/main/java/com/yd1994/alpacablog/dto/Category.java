package com.yd1994.alpacablog.dto;

import com.yd1994.alpacablog.entity.CategoryDO;

import java.io.Serializable;
import java.util.Date;

/**
 * dto 分类
 *
 * @author yd
 */
public class Category implements Serializable {

    private Long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类描述
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
    private Date mdified;

    public Category() {
    }

    public Category(CategoryDO categoryDO) {
        this(categoryDO.getId(),
                categoryDO.getName(),
                categoryDO.getDescription(),
                categoryDO.getAvailable(),
                categoryDO.getVersion(),
                categoryDO.getGmtCreated(),
                categoryDO.getGmtCreated());
    }

    public Category(Long id, String name, String description, Boolean available, Long version, Date created, Date mdified) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.version = version;
        this.created = created;
        this.mdified = mdified;
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

    public Date getMdified() {
        return mdified;
    }

    public void setMdified(Date mdified) {
        this.mdified = mdified;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", version=" + version +
                ", created=" + created +
                ", mdified=" + mdified +
                '}';
    }
}
