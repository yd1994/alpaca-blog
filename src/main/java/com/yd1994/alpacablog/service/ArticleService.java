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
     * 通过 RestRequestParam，categoryId 获取 Article
     *
     * sortByAsc, sortByDesc 排序
     * 可选参数 id, title, content, summary, traffic，created, modified 多个排序可用 created,modified
     * before, after 参数为时间 与 beforeBy, afterB 配套使用
     * beforeBy, afterBy 可选参数 created, modified 与 before, after配套使用
     *
     * categoryId: 可为空
     *
     * @param requestParam
     * @param categoryId 分类id
     * @return
     */
    ResultFactory.Collection<Article> list(RestRequestParam requestParam, Long categoryId);

    /**
     * 通过 RestRequestParam 获取 分类：categoryId 下的  Article
     *
     * sortByAsc, sortByDesc 可选参数 id, title, content, summary, traffic
     * beforeBy, afterBy 可选参数 created, modified
     *
     * @param categoryId 分类ID
     * @param requestParam
     * @return
     */
    ResultFactory.Collection<Article> listByCategoryId(Long categoryId, RestRequestParam requestParam);

}
