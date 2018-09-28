package com.yd1994.alpacablog;

import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.dto.Category;
import com.yd1994.alpacablog.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CategoryService categoryService;

    @Test
    public void get() {
        Long id = 1L;
        Category category = this.categoryService.get(id);
        logger.info(category.toString());
    }
}
