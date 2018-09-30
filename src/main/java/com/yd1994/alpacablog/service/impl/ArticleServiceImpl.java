package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.common.exception.TableVersionNotFoundException;
import com.yd1994.alpacablog.common.exception.VersionNotFoundException;
import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.entity.ArticleDO;
import com.yd1994.alpacablog.repository.ArticleRepository;
import com.yd1994.alpacablog.service.ArticleService;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Article：" + id + " 不存在。");
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
        ArticleDO articleDO = article.toEntity();
        Date date = new Date();
        articleDO.setGmtCreated(date);
        articleDO.setGmtModified(date);
        articleDO = this.articleRepository.saveAndFlush(articleDO);
        if (articleDO != null) {
            return true;
        }
        return false;
    }

    @Override
    public void update(Article article, Long id) {
        Optional<ArticleDO> optionalArticleDO = this.articleRepository.findById(id);
        try {
            ArticleDO targetArticleDO = optionalArticleDO.get();
            ArticleDO sourceArticleDO = article.toEntity();
            this.copyForUpdate(sourceArticleDO, targetArticleDO);
            targetArticleDO.setGmtModified(new Date());
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Article：" + id + " 不存在。");
        }
    }

    /**
     * 更新， 将原来的entity不为空的值拷贝到目标entity里
     * @param sourceArticleDO 原来的entity
     * @param targetArticleDO 目标entity
     */
    private void copyForUpdate(ArticleDO sourceArticleDO, ArticleDO targetArticleDO) {
        if (sourceArticleDO.getVersion() != null) {
            if (targetArticleDO.getVersion() == null) {
                throw new TableVersionNotFoundException("表：ArticleDO中乐观锁：version为null。");
            }
            if (sourceArticleDO.getVersion() != targetArticleDO.getVersion()) {
                throw new StaleObjectStateException("Article:" + sourceArticleDO.getId() + "已经被修改", sourceArticleDO);
            }
            targetArticleDO.setVersion(sourceArticleDO.getVersion());
        } else {
            // 乐观锁不能为空
            throw new VersionNotFoundException("Article:" + sourceArticleDO.getId() + " version为null。");
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
    }

    @Override
    public void delete(Long id) {
        this.articleRepository.deleteById(id);
    }

}
