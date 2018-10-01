package com.yd1994.alpacablog.controller;

import com.yd1994.alpacablog.common.base.BaseRestController;
import com.yd1994.alpacablog.common.param.RestRequestParam;
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

    @GetMapping
    public List<Article> list(RestRequestParam requestParam) {
        return this.articleService.list(requestParam);
    }

}
