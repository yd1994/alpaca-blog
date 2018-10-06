package com.yd1994.alpacablog.dto;

import com.yd1994.alpacablog.common.base.BaseDTO;
import com.yd1994.alpacablog.entity.FileDO;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息
 */
public class FileInfo extends BaseDTO<FileDO> implements Serializable {

    /**
     * ID
     */
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

    public FileInfo() {
    }

    public FileInfo(FileDO fileDO) {
        if (fileDO != null) {
            BeanUtils.copyProperties(fileDO, this);
            // 时间转换
            this.created = fileDO.getGmtCreated();
            this.modified = fileDO.getGmtModified();
        }
    }

    @Override
    public FileDO toEntity() {
        FileDO fileDO = new FileDO();
        BeanUtils.copyProperties(this, fileDO);
        fileDO.setGmtCreated(this.getCreated());
        fileDO.setGmtModified(this.getModified());
        return fileDO;
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
        return "FileInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", version=" + version +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }
}
