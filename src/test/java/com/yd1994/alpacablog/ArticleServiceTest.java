package com.yd1994.alpacablog;

import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.entity.ArticleDO;
import com.yd1994.alpacablog.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleService articleService;

    @Test
    public void get() {
        Long id = 1L;
        Article article = this.articleService.get(id);
        logger.info(article.toString());
    }

    @Test
    public void update() {
        Long id = 1L;
        Article article = new Article();
        article.setSummary("test00003");
        article.setVersion(1L);
        articleService.update(article, id);
        logger.info(article.toString());
    }
}
