package com.yd1994.alpacablog.dto;

import com.yd1994.alpacablog.common.base.BaseDTO;
import com.yd1994.alpacablog.entity.SysInformationDO;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统信息
 *
 * @author yd
 */
public class SysInformation extends BaseDTO<SysInformationDO> implements Serializable {

    /**
     * 信息名
     */
    private String name;
    /**
     * 信息值
     */
    private String value;
    /**
     * 文件类型
     */
    private String type;
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

    public SysInformation() {
    }

    public SysInformation(SysInformationDO sysInformationDO) {
        if (sysInformationDO != null) {
            BeanUtils.copyProperties(sysInformationDO, this);
            // 时间转换
            this.created = sysInformationDO.getGmtCreated();
            this.modified = sysInformationDO.getGmtModified();
        }
    }

    @Override
    public SysInformationDO toEntity() {
        SysInformationDO sysInformationDO = new SysInformationDO();
        BeanUtils.copyProperties(this, sysInformationDO);
        sysInformationDO.setGmtCreated(this.getCreated());
        sysInformationDO.setGmtModified(this.getModified());
        return sysInformationDO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        return "SysInformation{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", version=" + version +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }
}
