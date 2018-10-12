package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.ArticleDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yd
 */
public interface ArticleRepository extends JpaRepository<ArticleDO, Long>, JpaSpecificationExecutor<ArticleDO> {

    /**
     * 查询　{@link ArticleDO}
     *
     * @param id articleDO的id {@link ArticleDO#id}
     * @param articleDODelete articleDO delete　{@link ArticleDO#delete}
     * @param categoryDODelete categoryDO delete　{@link com.yd1994.alpacablog.entity.CategoryDO#delete}
     * @return
     */
    ArticleDO findFirstByIdAndDeleteAndCategoryDODelete(Long id, Boolean articleDODelete, Boolean categoryDODelete);

}
