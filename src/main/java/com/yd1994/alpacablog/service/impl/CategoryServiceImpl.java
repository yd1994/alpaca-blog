package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.base.BaseServiceImpl;
import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.common.exception.TableVersionNotFoundException;
import com.yd1994.alpacablog.common.exception.VersionNotFoundException;
import com.yd1994.alpacablog.common.param.RestRequestParam;
import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.Category;
import com.yd1994.alpacablog.entity.ArticleDO;
import com.yd1994.alpacablog.entity.CategoryDO;
import com.yd1994.alpacablog.repository.CategoryRepository;
import com.yd1994.alpacablog.service.CategoryService;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl extends BaseServiceImpl<CategoryDO> implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Cacheable(key = "#id", unless = "#result == null")
    @Override
    public Category get(Long id) {
        Optional<CategoryDO> optionalCategoryDO = this.categoryRepository.findById(id);
        CategoryDO categoryDO = this.categoryRepository.findFirstByIdAndDelete(id, false);
        if (categoryDO == null) {
            throw new ResourceNotFoundException("Category：" + id + " 不存在。");
        }
        return new Category(categoryDO);
    }

    @Override
    public ResultFactory.Collection<Category> list(RestRequestParam requestParam) {
        Pageable pageable = requestParam.getPageable();
        Page<CategoryDO> categoryDOPage = this.categoryRepository.findAll(this.getRestSpecification(requestParam), pageable);
        List<Category> categoryList = new ArrayList<>(categoryDOPage.getContent().size());
        categoryDOPage.getContent().forEach(categoryDO -> categoryList.add(new Category(categoryDO)));
        return ResultFactory.getCollection(categoryList, categoryDOPage.getTotalElements());
    }


    @Override
    protected void addRestSpecificationPredicateList(List<Predicate> predicateList, Root<CategoryDO> root,
                                                     CriteriaBuilder criteriaBuilder, RestRequestParam requestParam) {
    }

    @Override
    public void add(Category category) {
        category.setId(null);
        CategoryDO categoryDO = category.toEntity();
        categoryDO.setAvailable(true);
        categoryDO.setDelete(false);
        Date date = new Date();
        categoryDO.setGmtCreated(date);
        categoryDO.setGmtModified(date);
        this.categoryRepository.save(categoryDO);
    }

    @CacheEvict(key = "#id")
    @Override
    public void update(Category category, Long id) {
        Optional<CategoryDO> optionalCategoryDO = this.categoryRepository.findById(id);
        try {
            CategoryDO targetCategoryDO = optionalCategoryDO.get();
            CategoryDO sourceCategoryDO = category.toEntity();
            this.copyForUpdate(sourceCategoryDO, targetCategoryDO);
            targetCategoryDO.setGmtModified(new Date());
            this.categoryRepository.save(targetCategoryDO);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Category：" + id + " 不存在。");
        }
    }


    /**
     * 修改前准备， 将原来的entity不为空的值拷贝到目标entity里
     * @param sourceCategoryDO 原来的entity
     * @param targetCategoryDO 目标entity
     */
    private void copyForUpdate(CategoryDO sourceCategoryDO, CategoryDO targetCategoryDO) {
        if (!StringUtils.isEmpty(sourceCategoryDO.getVersion())) {
            if (targetCategoryDO.getVersion() == null) {
                throw new TableVersionNotFoundException("表：ArticleDO中乐观锁：version为null。");
            }
            if (sourceCategoryDO.getVersion() != targetCategoryDO.getVersion()) {
                throw new StaleObjectStateException("Category:" + sourceCategoryDO.getId() + "已经被修改", sourceCategoryDO);
            }
            targetCategoryDO.setVersion(sourceCategoryDO.getVersion());
        } else {
            // 乐观锁不能为空
            throw new VersionNotFoundException("Category:" + sourceCategoryDO.getId() + " version为null。");
        }
        if (!StringUtils.isEmpty(sourceCategoryDO.getName())) {
            targetCategoryDO.setName(sourceCategoryDO.getName());
        }
        if (!StringUtils.isEmpty(sourceCategoryDO.getDescription())) {
            targetCategoryDO.setDescription(sourceCategoryDO.getDescription());
        }
        if (sourceCategoryDO.getAvailable() != null) {
            targetCategoryDO.setAvailable(sourceCategoryDO.getAvailable());
        }
    }

    @CacheEvict(key = "#id")
    @Override
    public void delete(Long id) {
        Optional<CategoryDO> optionalCategoryDO = this.categoryRepository.findById(id);
        try {
            CategoryDO targetCategoryDO = optionalCategoryDO.get();
            targetCategoryDO.setDelete(true);
            targetCategoryDO.setGmtModified(new Date());
            this.categoryRepository.save(targetCategoryDO);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Category：" + id + " 不存在。");
        }
    }

}
