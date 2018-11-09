package com.yd1994.alpacablog.controller;

import com.yd1994.alpacablog.common.base.BaseRestController;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.dto.Category;
import com.yd1994.alpacablog.service.ArticleService;
import com.yd1994.alpacablog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController extends BaseRestController<Category, CategoryService> {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResultFactory.Info list(RestRequestParam requestParam) {
        return ResultFactory.get200Info().data(this.categoryService.list(requestParam));
    }

    @GetMapping("/total")
    public ResultFactory.Info listTotal(RestRequestParam requestParam) {
        Long total = this.categoryService.listTotal(requestParam);
        return ResultFactory.get200Info().data(total);
    }

    @GetMapping("/{categoryId}/articles")
    public ResultFactory.Info listByCategoryId(RestRequestParam requestParam, @PathVariable("categoryId") Long categoryId) {
        return ResultFactory.get200Info().data(this.articleService.listByCategoryId(categoryId, requestParam));
    }

    @GetMapping("/{categoryId}/articles/total")
    public ResultFactory.Info listByCategoryIdTotal(RestRequestParam requestParam, @PathVariable("categoryId") Long categoryId) {
        Long total = this.articleService.total(requestParam, categoryId);
        return ResultFactory.get200Info().data(total);
    }
}
