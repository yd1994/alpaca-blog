package com.yd1994.alpacablog.controller;

import com.yd1994.alpacablog.common.base.BaseRestController;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.dto.Category;
import com.yd1994.alpacablog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController extends BaseRestController<Category, CategoryService> {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> list(RestRequestParam requestParam) {
        return this.categoryService.list(requestParam);
    }

}
