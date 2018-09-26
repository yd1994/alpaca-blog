package com.yd1994.alpacablog.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 博文表
 *
 * @author yd
 */
@Entity
@Table(name = "alpaca_blog_article")
public class ArticleDO implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 标题
     */
    @Column
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 简介
     */
    private String summary;
    /**
     * 点击量
     */
    private Long traffic;
    /**
     * 是否置顶
     */
    @Column(name = "is_top")
    private Boolean top;
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
