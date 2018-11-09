package com.yd1994.alpacablog.service;

import com.yd1994.alpacablog.common.base.BaseService;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.entity.ArticleDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Article service 接口
 *
 * @author yd
 */
public interface ArticleService extends BaseService<Article> {

    /**
     * 通过 RestRequestParam，categoryId 获取 Article的总数量
     *
     * @param requestParam
     * @param categoryId
     * @return
     */
    public Long total(RestRequestParam requestParam, Long categoryId);

    /**
     * 查询博文集合
     *
     * 可传递参数：sortByAsc, sortByDesc, before, beforeBy, after, afterBy, page, size, categoryId
     *
     * page 分页。页码。默认1。
     * size 分页。每页记录。默认10。
     *
     * sortByAsc, sortByDesc 排序
     * sortByAsc 正序
     * sortByDesc 反序
     * 可选参数 id, title, content, summary, traffic，created, modified
     * 多个排序可用 created,modified （SQL同规则）
     *
     * before、beforeBy， after、afterBy 筛选在什么时间之前/之后的记录
     *   before、beforeBy 必须同时存在。在什么时候之前的记录
     *     before 时间
     *     beforeBy 什么规则，可选created（创建时间），modified（最后修改时间）
     *   after、afterBy 必须同时存在。在什么时候之后的记录
     *     after 时间
     *     afterBy 什么规则，可选created（创建时间），modified（最后修改时间）
     *
     * view 用于搜索，可搜索字段 title, content
     *
     * categoryId 分类id，通过分类id筛选
     *
     * @param categoryId 分类ID
     * @param requestParam
     * @return
     */
    ResultFactory.CollectionData<Article> listByCategoryId(Long categoryId, RestRequestParam requestParam);

    /**
     * 添加点击量
     * @param id
     */
    Long addAndGetArticleTraffic(Long id);

}
