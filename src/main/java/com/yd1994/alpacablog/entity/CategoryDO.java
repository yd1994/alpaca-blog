package com.yd1994.alpacablog.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 分类
 *
 * @author yd
 */
@Entity
@Table(name = "alpaca_blog_category")
public class CategoryDO implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "is_available")
    private Boolean available = true;
    /**
     * 是否被删除
     */
    @Column(name = "is_delete")
    private Boolean delete = false;
    /**
     * 乐观锁
     */
    @Version
    private Long version = 1L;
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

    @Override
    public String toString() {
        return "CategoryDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", delete=" + delete +
                ", version=" + version +
                ", gmtCreated=" + gmtCreated +
                ", createdBy='" + createdBy + '\'' +
                ", gmtModified=" + gmtModified +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
