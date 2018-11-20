package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.base.BaseServiceImpl;
import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.common.exception.TableVersionNotFoundException;
import com.yd1994.alpacablog.common.exception.VersionNotFoundException;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.ArticleTag;
import com.yd1994.alpacablog.entity.ArticleTagDO;
import com.yd1994.alpacablog.repository.ArticleTagRepository;
import com.yd1994.alpacablog.service.ArticleTagService;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "articleTag")
public class ArticleTagServiceImpl extends BaseServiceImpl<ArticleTagDO> implements ArticleTagService {

    @Autowired
    private ArticleTagRepository articleTagRepository;

    @Cacheable(key = "#id", unless = "#result == null")
    @Override
    public ArticleTag get(Long id) {
        ArticleTagDO categoryDO = this.articleTagRepository.findFirstByIdAndDelete(id, false);
        if (categoryDO == null) {
            throw new ResourceNotFoundException("ArticleTag：" + id + " 不存在。");
        }
        return new ArticleTag(categoryDO);
    }

    @Override
    public Long listTotal(RestRequestParam requestParam) {
        return this.articleTagRepository.count(this.getRestSpecification(requestParam));
    }

    @Override
    public ResultFactory.CollectionData<ArticleTag> list(RestRequestParam requestParam) {
        Pageable pageable = requestParam.getPageable();
        Page<ArticleTagDO> categoryDOPage = this.articleTagRepository.findAll(this.getRestSpecification(requestParam), pageable);
        List<ArticleTag> categoryList = new ArrayList<>(categoryDOPage.getContent().size());
        categoryDOPage.getContent().forEach(categoryDO -> categoryList.add(new ArticleTag(categoryDO)));
        return ResultFactory.getCollectionData(categoryList, categoryDOPage.getTotalElements());
    }


    @Override
    protected void addRestSpecificationPredicateList(List<Predicate> predicateList, Root<ArticleTagDO> root,
                                                     CriteriaBuilder criteriaBuilder, RestRequestParam requestParam) {
    }

    @Override
    public void add(ArticleTag articleTag) {
        articleTag.setId(null);
        ArticleTagDO articleTagDO = articleTag.toEntity();
        articleTagDO.setAvailable(true);
        articleTagDO.setDelete(false);
        Date date = new Date();
        articleTagDO.setGmtCreated(date);
        articleTagDO.setGmtModified(date);
        this.articleTagRepository.save(articleTagDO);
    }

    @CacheEvict(key = "#id")
    @Override
    public void update(ArticleTag category, Long id) {
        ArticleTagDO categoryDO = this.articleTagRepository.findFirstByIdAndDelete(id, false);
        if (categoryDO == null) {
            throw new ResourceNotFoundException("ArticleTag：" + id + " 不存在。");
        }
        ArticleTagDO targetArticleTagDO = categoryDO;
        ArticleTagDO sourceArticleTagDO = category.toEntity();
        this.copyForUpdate(sourceArticleTagDO, targetArticleTagDO);
        targetArticleTagDO.setGmtModified(new Date());
        this.articleTagRepository.save(targetArticleTagDO);
    }


    /**
     * 修改前准备， 将原来的entity不为空的值拷贝到目标entity里
     * @param sourceArticleTagDO 原来的entity
     * @param targetArticleTagDO 目标entity
     */
    private void copyForUpdate(ArticleTagDO sourceArticleTagDO, ArticleTagDO targetArticleTagDO) {
        if (!StringUtils.isEmpty(sourceArticleTagDO.getVersion())) {
            if (targetArticleTagDO.getVersion() == null) {
                throw new TableVersionNotFoundException("表：ArticleDO中乐观锁：version为null。");
            }
            if (sourceArticleTagDO.getVersion() != targetArticleTagDO.getVersion()) {
                throw new StaleObjectStateException("ArticleTag:" + sourceArticleTagDO.getId() + "已经被修改", sourceArticleTagDO);
            }
            targetArticleTagDO.setVersion(sourceArticleTagDO.getVersion());
        } else {
            // 乐观锁不能为空
            throw new VersionNotFoundException("ArticleTag:" + sourceArticleTagDO.getId() + " version为null。");
        }
        if (!StringUtils.isEmpty(sourceArticleTagDO.getName())) {
            targetArticleTagDO.setName(sourceArticleTagDO.getName());
        }
        if (!StringUtils.isEmpty(sourceArticleTagDO.getDescription())) {
            targetArticleTagDO.setDescription(sourceArticleTagDO.getDescription());
        }
        if (sourceArticleTagDO.getAvailable() != null) {
            targetArticleTagDO.setAvailable(sourceArticleTagDO.getAvailable());
        }
    }

    @CacheEvict(key = "#id")
    @Override
    public void delete(Long id) {
        ArticleTagDO categoryDO = this.articleTagRepository.findFirstByIdAndDelete(id, false);
        if (categoryDO == null) {
            throw new ResourceNotFoundException("ArticleTag：" + id + " 不存在。");
        }
        categoryDO.setDelete(true);
        categoryDO.setGmtModified(new Date());
        this.articleTagRepository.save(categoryDO);
    }

}
