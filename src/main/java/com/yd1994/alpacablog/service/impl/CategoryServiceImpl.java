package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.exception.SourceNotFoundException;
import com.yd1994.alpacablog.dto.Category;
import com.yd1994.alpacablog.entity.CategoryDO;
import com.yd1994.alpacablog.repository.CategoryRepository;
import com.yd1994.alpacablog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category get(Long id) {
        Optional<CategoryDO> optionalCategoryDO = this.categoryRepository.findById(id);
        try {
            CategoryDO categoryDO = optionalCategoryDO.get();
            return new Category(categoryDO);
        } catch (NullPointerException e) {
            throw new SourceNotFoundException("分类：" + id + " 不存在。");
        }
    }

    @Override
    public List<Category> list(Pageable pageable) {
        Page<CategoryDO> page = this.categoryRepository.findAll(pageable);
        List<Category> categoryList = new ArrayList<>(page.getSize() + 1);
        page.getContent().stream().forEach(categoryDO -> categoryList.add(new Category(categoryDO)));
        return categoryList;
    }

    @Override
    public boolean add(Category category) {
        category.setId(null);
        CategoryDO categoryDO = this.categoryRepository.save(category.toEntity());
        if (categoryDO == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Category category, Long id) {
        CategoryDO targetCategoryDO = this.categoryRepository.findById(id).get();
        if (targetCategoryDO == null) {
            return false;
        }
        CategoryDO sourceCategoryDO = category.toEntity();
        if (!this.copyForUpdate(sourceCategoryDO, targetCategoryDO)) {
            return false;
        }
        targetCategoryDO.setGmtModified(new Date());
        this.categoryRepository.save(targetCategoryDO);
        return false;
    }


    /**
     * 修改前准备， 将原来的entity不为空的值拷贝到目标entity里
     * @param sourceCategoryDO 原来的entity
     * @param targetCategoryDO 目标entity
     */
    private boolean copyForUpdate(CategoryDO sourceCategoryDO, CategoryDO targetCategoryDO) {
        if (sourceCategoryDO == null) {
            return false;
        }
        if (targetCategoryDO == null) {
            return false;
        }
        if (!StringUtils.isEmpty(sourceCategoryDO.getVersion())) {
            targetCategoryDO.setVersion(sourceCategoryDO.getVersion());
        } else {
            // 乐观锁不能为空
            return false;
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
        return true;
    }

    @Override
    public boolean delete(Long id) {
        this.categoryRepository.deleteById(id);
        return true;
    }
}
