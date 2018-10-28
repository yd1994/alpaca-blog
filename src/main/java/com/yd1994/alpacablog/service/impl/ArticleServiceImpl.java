package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.base.BaseServiceImpl;
import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.common.exception.TableVersionNotFoundException;
import com.yd1994.alpacablog.common.exception.VersionNotFoundException;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.entity.ArticleDO;
import com.yd1994.alpacablog.entity.CategoryDO;
import com.yd1994.alpacablog.repository.ArticleRepository;
import com.yd1994.alpacablog.repository.CategoryRepository;
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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
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
    @Autowired
    private CategoryRepository categoryRepository;

    @Cacheable(key = "#id", unless = "#result == null")
    @Override
    public Article get(Long id) {
        ArticleDO articleDO = this.articleRepository.findFirstByIdAndDeleteAndCategoryDODelete(id, false, false);
        if (articleDO == null) {
            throw new ResourceNotFoundException("Article：" + id + " 不存在。");
        }
        this.addArticleTraffic(id);
        return new Article(articleDO);
    }

    @Override
    public Long total(RestRequestParam requestParam, Long categoryId) {
        Specification<ArticleDO> restRequestParamSpecification = this.getRestSpecification(requestParam);
        Specification<ArticleDO> categorySpecification = this.getSpecificationForCategory(categoryId);
        Long articleTotal = this.articleRepository.count(
                Specification.where(restRequestParamSpecification).and(categorySpecification));
        return articleTotal;
    }

    private Specification<ArticleDO> getSpecificationForCategory(Long categoryId) {
        Specification<ArticleDO> categorySpecification = (Specification<ArticleDO>) (root, query, criteriaBuilder) -> {
            if (categoryId != null && categoryId > 0) {
                Predicate idPredicate = criteriaBuilder.equal(root.join("categoryDO").get("id"), categoryId);
                Predicate deletePredicate = criteriaBuilder.equal(root.join("categoryDO").get("delete"), false);
                return criteriaBuilder.equal(root.join("categoryDO").get("id"), categoryId);
            }
            return null;
        };
        return categorySpecification;
    }

    @Override
    protected void addRestSpecificationPredicateList(List<Predicate> predicateList, Root<ArticleDO> root,
                                                     CriteriaBuilder criteriaBuilder, RestRequestParam requestParam) {
        if (!StringUtils.isEmpty(requestParam.getView())) {
            Predicate titlePredicate = criteriaBuilder.like(root.get("title")
                    .as(String.class), "%" + requestParam.getView() + "%");
            Predicate contentPredicate = criteriaBuilder.like(root.get("content")
                            .as(String.class), "%" + requestParam.getView() + "%");
            Predicate p = criteriaBuilder.or(titlePredicate, contentPredicate);
            predicateList.add(p);
        }
    }

    @Override
    public ResultFactory.Collection<Article> list(RestRequestParam requestParam, Long categoryId) {
        return this.listByCategoryId(categoryId, requestParam);
    }

    @Override
    public ResultFactory.Collection<Article> listByCategoryId(Long categoryId, RestRequestParam requestParam) {
        Pageable pageable = requestParam.getPageable();
        Specification<ArticleDO> restRequestParamSpecification = this.getRestSpecification(requestParam);
        Specification<ArticleDO> categorySpecification = this.getSpecificationForCategory(categoryId);
        Page<ArticleDO> articlePage = this.articleRepository.findAll(
                Specification.where(restRequestParamSpecification).and(categorySpecification), pageable);
        List<Article> articleList = new ArrayList<>(articlePage.getContent().size());
        articlePage.getContent().forEach(articleDO -> articleList.add(new Article(articleDO)));
        return ResultFactory.getCollection(articleList, articlePage.getTotalElements());
    }

    @Override
    public void add(Article article) {
        ArticleDO articleDO = article.toEntity();
        articleDO.setTraffic(0L);
        articleDO.setTop(false);
        articleDO.setDelete(false);
        articleDO.setVersion(null);
        Date date = new Date();
        articleDO.setGmtCreated(date);
        articleDO.setGmtModified(date);
        if (articleDO.getCategoryDO() != null && articleDO.getCategoryDO().getId() != null) {
            CategoryDO categoryDO = this.categoryRepository.findFirstByIdAndDelete(articleDO.getCategoryDO().getId(), false);
            articleDO.setCategoryDO(categoryDO);
        }
        this.articleRepository.saveAndFlush(articleDO);
    }

    @CacheEvict(key = "#id")
    @Override
    public void update(Article article, Long id) {
        ArticleDO articleDO = this.articleRepository.findFirstByIdAndDeleteAndCategoryDODelete(id, false, false);
        if (articleDO == null) {
            throw new ResourceNotFoundException("Article：" + id + " 不存在。");
        }
        ArticleDO targetArticleDO = articleDO;
        ArticleDO sourceArticleDO = article.toEntity();
        this.copyForUpdate(sourceArticleDO, targetArticleDO);
        targetArticleDO.setGmtModified(new Date());
        if (targetArticleDO.getCategoryDO() != null && targetArticleDO.getCategoryDO().getId() != null) {
            CategoryDO categoryDO = this.categoryRepository.findFirstByIdAndDelete(targetArticleDO.getCategoryDO().getId(), false);
            targetArticleDO.setCategoryDO(categoryDO);
        }
        this.articleRepository.save(targetArticleDO);
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
        if (sourceArticleDO.getCategoryDO() != null) {
            targetArticleDO.setCategoryDO(sourceArticleDO.getCategoryDO());
        }
    }

    @CacheEvict(key = "#id")
    @Override
    public void addArticleTraffic(Long id) {
        this.articleRepository.addArticleTraffic(id);
    }

    @CacheEvict(key = "#id")
    @Override
    public void delete(Long id) {
        ArticleDO articleDO = this.articleRepository.findFirstByIdAndDeleteAndCategoryDODelete(id, false, false);
        if (articleDO == null) {
            throw new ResourceNotFoundException("Article：" + id + " 不存在。");
        }
        articleDO.setDelete(true);
        articleDO.setGmtModified(new Date());
        this.articleRepository.save(articleDO);
    }

}
