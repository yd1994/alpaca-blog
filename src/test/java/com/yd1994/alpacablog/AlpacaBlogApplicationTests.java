package com.yd1994.alpacablog;

import com.yd1994.alpacablog.entity.ArticleDO;
import com.yd1994.alpacablog.entity.AuthOauthClientDetails;
import com.yd1994.alpacablog.repository.ArticleRepository;
import com.yd1994.alpacablog.repository.AuthOauthClientDetailsRepository;
import com.yd1994.alpacablog.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlpacaBlogApplicationTests {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthOauthClientDetailsRepository authOauthClientDetailsRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testBCryptPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode("123456");
        logger.info(encodePassword);
    }

    @Test
    public void testArticleRepositoryAdd() {
        ArticleDO articleDO = new ArticleDO();
        articleDO.setTitle("静夜思");
        articleDO.setSummary("唐朝诗人 李白 字太白");
        articleDO.setContent("<p>床前明月光</p>，<p>疑是地上霜</p>。<p>举头望明月</p>，<p>低头思故乡</p>。");
        Date date = new Date();
        articleDO.setGmtCreated(date);
        articleDO.setCreatedBy("yd");
        articleDO.setGmtModified(date);
        articleDO.setModifiedBy("yd");
        this.articleRepository.save(articleDO);
    }

    @Test
    public void testArticleRepositoryGet() {
        Pageable pageable = new PageRequest(0, 10);
        Page<ArticleDO> page = this.articleRepository.findAll(pageable);
        List<ArticleDO> articleDOList = page.getContent();
        articleDOList.stream().forEach(articleDO -> logger.info(articleDO.toString()));
    }

    @Test
    public void testArticleRepositoryUpdate() {
        ArticleDO articleDO = this.articleRepository.findById(1L).orElse(null);
        logger.info(articleDO.toString());
        articleDO.setTitle("Hello world");
        articleDO.setVersion(1L);
        this.articleRepository.save(articleDO);
    }

    @Test
    public void testAuthOauthClientDetailsRepository() {
        AuthOauthClientDetails authOauthClientDetails = this.authOauthClientDetailsRepository.findFirstByClientId("client1");
        logger.info(authOauthClientDetails.toString());
    }

}
