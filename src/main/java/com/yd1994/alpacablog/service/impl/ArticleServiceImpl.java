package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.exception.SourceNotFoundException;
import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.entity.ArticleDO;
import com.yd1994.alpacablog.repository.ArticleRepository;
import com.yd1994.alpacablog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * ArticleService 实现类
 *
 * @author yd
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article get(Long id) {
        Optional<ArticleDO> optionalArticleDO = this.articleRepository.findById(id);
        try {
            ArticleDO articleDO = optionalArticleDO.get();
            return new Article(articleDO);
        } catch (NullPointerException e) {
            throw new SourceNotFoundException("博文：" + id + " 不存在。");
        }
    }

    @Override
    public List<Article> list(Pageable pageable) {
        Page<ArticleDO> page = this.articleRepository.findAll(pageable);
        List<Article> articleList = new ArrayList<>(page.getSize() + 1);
        page.getContent().stream().forEach(articleDO -> articleList.add(new Article(articleDO)));
        return articleList;
    }

    @Override
    public boolean add(Article article) {
        ArticleDO articleDO = this.articleRepository.save(article.toEntity());
        if (articleDO != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Article article, Long id) {
        ArticleDO targetArticleDO = this.articleRepository.findById(id).get();
        if (targetArticleDO == null) {
            return false;
        }
        ArticleDO sourceArticleDO = article.toEntity();
        if (!this.copyForUpdate(targetArticleDO, sourceArticleDO)) {
            return false;
        }
        targetArticleDO.setGmtModified(new Date());
        this.articleRepository.save(targetArticleDO);
        return true;
    }

    /**
     * 修改前准备， 将原来的entity不为空的值拷贝到目标entity里
     * @param sourceArticleDO 原来的entity
     * @param targetArticleDO 目标entity
     */
    private boolean copyForUpdate(ArticleDO sourceArticleDO, ArticleDO targetArticleDO) {
        if (sourceArticleDO == null) {
            return false;
        }
        if (targetArticleDO == null) {
            return false;
        }
        if (!StringUtils.isEmpty(sourceArticleDO.getVersion())) {
            targetArticleDO.setVersion(sourceArticleDO.getVersion());
        } else {
            // 乐观锁不能为空
            return false;
        }
        if (!StringUtils.isEmpty(sourceArticleDO.getTitle())) {
            targetArticleDO.setTitle(sourceArticleDO.getTitle());
        }
        if (!StringUtils.isEmpty(sourceArticleDO.getContent())) {
            targetArticleDO.setContent(sourceArticleDO.getContent());
        }
        if (!StringUtils.isEmpty(sourceArticleDO.getSummary())) {
            targetArticleDO.setSummary(sourceArticleDO.getSummary());
        }
        if (sourceArticleDO.getTop() != null) {
            targetArticleDO.setTop(sourceArticleDO.getTop());
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
        this.articleRepository.deleteById(id);
        return true;
    }

}
