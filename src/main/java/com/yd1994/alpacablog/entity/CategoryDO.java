package com.yd1994.alpacablog.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    private Boolean available;
    /**
     * 是否被删除
     */
    @Column(name = "is_delete")
    private Boolean delete;
    /**
     * 乐观锁
     */
    @Version
    private Long version;
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

}
