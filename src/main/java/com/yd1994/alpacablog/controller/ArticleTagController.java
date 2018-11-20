package com.yd1994.alpacablog.controller;

import com.yd1994.alpacablog.common.base.BaseRestController;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.ArticleTag;
import com.yd1994.alpacablog.dto.Category;
import com.yd1994.alpacablog.service.ArticleService;
import com.yd1994.alpacablog.service.ArticleTagService;
import com.yd1994.alpacablog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articleTags")
public class ArticleTagController extends BaseRestController<ArticleTag, ArticleTagService> {

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResultFactory.Info list(RestRequestParam requestParam) {
        return ResultFactory.get200Info().data(this.articleTagService.list(requestParam));
    }

    @GetMapping("/total")
    public ResultFactory.Info listTotal(RestRequestParam requestParam) {
        Long total = this.articleTagService.listTotal(requestParam);
        return ResultFactory.get200Info().data(total);
    }

    @GetMapping("/{articleTagId}/articles")
    public ResultFactory.Info listByCategoryId(RestRequestParam requestParam, @PathVariable("articleTagId") Long articleTagId) {
        return ResultFactory.get200Info().data(this.articleService.listByCategoryId(articleTagId, requestParam));
    }

    @GetMapping("/{articleTagId}/articles/total")
    public ResultFactory.Info listByCategoryIdTotal(RestRequestParam requestParam, @PathVariable("articleTagId") Long articleTagId) {
        Long total = this.articleService.totalByCategoryId(requestParam, articleTagId);
        return ResultFactory.get200Info().data(total);
    }
}
