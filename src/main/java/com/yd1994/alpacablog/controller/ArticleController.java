package com.yd1994.alpacablog.controller;

import com.yd1994.alpacablog.common.base.BaseRestController;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author yd
 */
@RestController
@RequestMapping("/articles")
public class ArticleController extends BaseRestController<Article, ArticleService> {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/{id}")
    @Override
    public ResultFactory.Info get(@PathVariable("id") Long id) {
        Article article = this.articleService.get(id);
        article.setTraffic(this.articleService.addAndGetArticleTraffic(id));
        return ResultFactory.get200Info().data(article);
    }

    /**
     * 查询博文集合
     * <p>
     * 可传递参数：sortByAsc, sortByDesc, before, beforeBy, after, afterBy, page, size, categoryId
     * <p>
     * page 分页。页码。默认1。
     * size 分页。每页记录。默认10。
     * <p>
     * sortByAsc, sortByDesc 排序
     * sortByAsc 正序
     * sortByDesc 反序
     * 可选参数 id, title, content, summary, traffic，created, modified
     * 多个排序可用 created,modified （SQL同规则）
     * <p>
     * before、beforeBy， after、afterBy 筛选在什么时间之前/之后的记录
     * before、beforeBy 必须同时存在。在什么时候之前的记录
     * before 时间
     * beforeBy 什么规则，可选created（创建时间），modified（最后修改时间）
     * after、afterBy 必须同时存在。在什么时候之后的记录
     * after 时间
     * afterBy 什么规则，可选created（创建时间），modified（最后修改时间）
     * <p>
     * view 用于搜索，可搜索字段 title, content
     * <p>
     * categoryId 分类id，通过分类id筛选
     *
     * @param requestParam
     * @param categoryId
     * @return
     */
    @GetMapping
    public ResultFactory.Info list(RestRequestParam requestParam, Long categoryId) {
        return ResultFactory.get200Info().data(this.articleService.listByCategoryId(categoryId, requestParam));
    }

    /**
     * 查询博文集合总数
     * <p>
     * 参数: 同{@link ArticleController#list(RestRequestParam, Long)}
     *
     * @param requestParam
     * @param categoryId
     * @return
     */
    @GetMapping("/total")
    public ResultFactory.Info listTotal(RestRequestParam requestParam, Long categoryId) {
        Long total = this.articleService.total(requestParam, categoryId);
        return ResultFactory.get200Info().data(total);
    }

}
