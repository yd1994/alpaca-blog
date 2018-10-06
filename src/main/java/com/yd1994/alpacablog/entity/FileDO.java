package com.yd1994.alpacablog.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "alpaca_blog_file")
public class FileDO implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件类型
     * 0：图片
     */
    private Integer type;
    /**
     * 文件地址
     */
    private String path;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        return "FileDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", delete=" + delete +
                ", version=" + version +
                ", gmtCreated=" + gmtCreated +
                ", createdBy='" + createdBy + '\'' +
                ", gmtModified=" + gmtModified +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
