package com.yd1994.alpacablog.controller;

import com.yd1994.alpacablog.common.base.BaseRestController;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController extends BaseRestController<Article, ArticleService> {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取 Article 集合
     * page 默认：1 必须 大于 0
     * size 默认：10 必须 大于 0
     * sortByAsc, sortByDesc 可选参数 id, title, content, summary, traffic, created, modified
     * beforeBy, afterBy 可选参数 created, modified
     *
     * @param requestParam
     * @return
     */
    @GetMapping
    public ResultFactory.Collection<Article> list(RestRequestParam requestParam, Long categoryId) {
        return this.articleService.list(requestParam, categoryId);
    }

}
