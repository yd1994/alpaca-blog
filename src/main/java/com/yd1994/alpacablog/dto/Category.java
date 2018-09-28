package com.yd1994.alpacablog.dto;

import com.yd1994.alpacablog.common.base.BaseDTO;
import com.yd1994.alpacablog.entity.ArticleDO;
import com.yd1994.alpacablog.entity.CategoryDO;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * dto 分类
 *
 * @author yd
 */
public class Category extends BaseDTO<CategoryDO> implements Serializable {

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
    private Date modified;

    public Category() {
    }

    /**
     * 通过CategoryDO创建
     * @param categoryDO
     */
    public Category(CategoryDO categoryDO) {
        if (categoryDO !=null) {
            BeanUtils.copyProperties(categoryDO, this);
            // 时间转换
            this.created = categoryDO.getGmtCreated();
            this.modified = categoryDO.getGmtModified();
        }
    }

    public Category(Long id, String name, String description, Boolean available, Long version, Date created, Date modified) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.version = version;
        this.created = created;
        this.modified = modified;
    }


    /**
     * 转换为CategoryDO
     * @return
     */
    @Override
    public CategoryDO toEntity() {
        CategoryDO categoryDO = new CategoryDO();
        BeanUtils.copyProperties(this, categoryDO);
        categoryDO.setGmtCreated(this.getCreated());
        categoryDO.setGmtModified(this.getModified());
        return categoryDO;
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
        return "Category{" +
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
