package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.base.BaseServiceImpl;
import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.common.exception.TableVersionNotFoundException;
import com.yd1994.alpacablog.common.exception.VersionNotFoundException;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.Article;
import com.yd1994.alpacablog.entity.ArticleDO;
import com.yd1994.alpacablog.entity.ArticleTagDO;
import com.yd1994.alpacablog.entity.CategoryDO;
import com.yd1994.alpacablog.repository.ArticleRepository;
import com.yd1994.alpacablog.repository.ArticleTagRepository;
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
import org.springframework.data.redis.core.RedisTemplate;
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
    private ArticleTagRepository articleTagRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Cacheable(key = "#id", unless = "#result == null")
    @Override
    public Article get(Long id) {
        ArticleDO articleDO = this.articleRepository.findFirstByIdAndDeleteAndCategoryDODelete(id, false, false);
        if (articleDO == null) {
            throw new ResourceNotFoundException("Article：" + id + " 不存在。");
        }
        String trafficNameInRedis = "articles::" + id + ":traffic";
        Object traffic = this.redisTemplate.opsForValue().get(trafficNameInRedis);
        if (traffic == null) {
            this.articleRepository.setArticleTraffic(id, articleDO.getTraffic());
        }
        return new Article(articleDO);
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

    private Specification<ArticleDO> getSpecificationForArticleTag(Long articleTagId) {
        Specification<ArticleDO> articleTagSpecification = (Specification<ArticleDO>) (root, query, criteriaBuilder) -> {
            if (articleTagId != null && articleTagId > 0) {
                Predicate idPredicate = criteriaBuilder.equal(root.join("articleTagDO").get("id"), articleTagId);
                Predicate deletePredicate = criteriaBuilder.equal(root.join("articleTagDO").get("delete"), false);
                return criteriaBuilder.equal(root.join("articleTagDO").get("id"), articleTagId);
            }
            return null;
        };
        return articleTagSpecification;
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
    public Long totalByCategoryId(RestRequestParam requestParam, Long categoryId) {
        Specification<ArticleDO> restRequestParamSpecification = this.getRestSpecification(requestParam);
        Specification<ArticleDO> categorySpecification = this.getSpecificationForCategory(categoryId);
        Long articleTotal = this.articleRepository.count(
                Specification.where(restRequestParamSpecification).and(categorySpecification));
        return articleTotal;
    }

    // @Cacheable(keyGenerator = "customKeyGenerator", unless = "#result == null")
    // 在添加、修改或删除后，如何高效的清除缓存待考虑
    @Override
    public ResultFactory.CollectionData<Article> listByCategoryId(Long categoryId, RestRequestParam requestParam) {
        Pageable pageable = requestParam.getPageable();

        Specification<ArticleDO> restRequestParamSpecification = this.getRestSpecification(requestParam);
        Specification<ArticleDO> categorySpecification = this.getSpecificationForCategory(categoryId);

        Page<ArticleDO> articlePage = this.articleRepository.findAll(
                Specification.where(restRequestParamSpecification).and(categorySpecification), pageable);

        List<Article> articleList = new ArrayList<>(articlePage.getContent().size());
        articlePage.getContent().forEach(articleDO -> articleList.add(new Article(articleDO)));

        return ResultFactory.getCollectionData(articleList, articlePage.getTotalElements());
    }

    @Override
    public Long totalByArticleTagId(RestRequestParam requestParam, Long articleTagId) {
        Specification<ArticleDO> restRequestParamSpecification = this.getRestSpecification(requestParam);
        Specification<ArticleDO> articleTagSpecification = this.getSpecificationForArticleTag(articleTagId);
        Long articleTotal = this.articleRepository.count(
                Specification.where(restRequestParamSpecification).and(articleTagSpecification));
        return articleTotal;
    }

    @Override
    public ResultFactory.CollectionData<Article> listByArticleTagId(Long articleTagId, RestRequestParam requestParam) {
        Pageable pageable = requestParam.getPageable();

        Specification<ArticleDO> restRequestParamSpecification = this.getRestSpecification(requestParam);
        Specification<ArticleDO> categorySpecification = this.getSpecificationForArticleTag(articleTagId);

        Page<ArticleDO> articlePage = this.articleRepository.findAll(
                Specification.where(restRequestParamSpecification).and(categorySpecification), pageable);

        List<Article> articleList = new ArrayList<>(articlePage.getContent().size());
        articlePage.getContent().forEach(articleDO -> articleList.add(new Article(articleDO)));

        return ResultFactory.getCollectionData(articleList, articlePage.getTotalElements());
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
        if (articleDO.getArticleTagDOList() != null) {
            List<ArticleTagDO> articleTagDOList = articleDO.getArticleTagDOList();
            for (int i = 0; i < articleTagDOList.size(); i++) {
                if (articleTagDOList.get(i).getId() != null) {
                    articleTagDOList.set(i, this.articleTagRepository.findFirstByIdAndDelete(articleTagDOList.get(i).getId(), false));
                }
            }
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
        if (targetArticleDO.getArticleTagDOList() != null) {
            List<ArticleTagDO> articleTagDOList = targetArticleDO.getArticleTagDOList();
            for (int i = 0; i < articleTagDOList.size(); i++) {
                if (articleTagDOList.get(i).getId() != null) {
                    articleTagDOList.set(i, this.articleTagRepository.findFirstByIdAndDelete(articleTagDOList.get(i).getId(), false));
                }
            }
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
        if (sourceArticleDO.getArticleTagDOList() != null) {
            targetArticleDO.setArticleTagDOList(sourceArticleDO.getArticleTagDOList());
        }
    }

    @Override
    public Long addAndGetArticleTraffic(Long id) {
        String trafficNameInRedis = "articles::" + id + ":traffic";
        Long traffic = (Long) this.redisTemplate.opsForValue().increment(trafficNameInRedis, 1L);
        if (traffic % 10 == 0) {
            // 当访问量为10的倍数，持久化到
            this.articleRepository.setArticleTraffic(id, traffic);
        }
        return traffic;
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
