package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.base.BaseServiceImpl;
import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.common.exception.TableVersionNotFoundException;
import com.yd1994.alpacablog.common.exception.VersionNotFoundException;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.entity.ArticleDO;
import com.yd1994.alpacablog.repository.ArticleRepository;
import com.yd1994.alpacablog.service.ArticleService;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * ArticleService 实现类
 *
 * @author yd
 */
@Service
@CacheConfig(cacheNames = "articles")
public class ArticleServiceImpl extends BaseServiceImpl<ArticleDO> implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Cacheable(key = "#id", unless = "#result == null")
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
    protected void addRestSpecificationPredicateList(List<Predicate> predicateList, Root<ArticleDO> root, CriteriaBuilder criteriaBuilder) {
    }

    @Override
    public List<Article> listByCategoryId(RestRequestParam requestParam) {
        Pageable pageable = requestParam.getPageable();
        Page<ArticleDO> articlePage = this.articleRepository.findAll(this.getRestSpecification(requestParam), pageable);
        List<Article> articleList = new ArrayList<>(articlePage.getContent().size());
        articlePage.getContent().forEach(articleDO -> articleList.add(new Article(articleDO)));
        return articleList;
    }

    @Override
    public List<Article> listByCategoryId(Long categoryId, RestRequestParam requestParam) {
        Pageable pageable = requestParam.getPageable();
        Specification<ArticleDO> restRequestParamSpecification = this.getRestSpecification(requestParam);
        Specification<ArticleDO> categorySpecification = (Specification<ArticleDO>) (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.join("categoryDO").get("id"), categoryId);
        };
        Page<ArticleDO> articlePage = this.articleRepository.findAll(
                Specification.where(restRequestParamSpecification).and(categorySpecification), pageable);
        List<Article> articleList = new ArrayList<>(articlePage.getContent().size());
        articlePage.getContent().forEach(articleDO -> articleList.add(new Article(articleDO)));
        return articleList;
    }

    @Override
    public void add(Article article) {
        ArticleDO articleDO = article.toEntity();
        articleDO.setTraffic(0L);
        articleDO.setTop(false);
        articleDO.setDelete(false);
        Date date = new Date();
        articleDO.setGmtCreated(date);
        articleDO.setGmtModified(date);
        this.articleRepository.saveAndFlush(articleDO);
    }

    @CachePut(key = "#id")
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

    @CacheEvict(key = "#id")
    @Override
    public void delete(Long id) {
        this.articleRepository.deleteById(id);
    }

}
