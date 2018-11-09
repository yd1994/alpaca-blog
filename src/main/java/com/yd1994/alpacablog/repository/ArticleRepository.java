package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.ArticleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

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

    /**
     * 设置阅读量
     *
     * @param id {@link ArticleDO#id}
     * @param traffic {@link ArticleDO#traffic}
     */
    @Transactional
    @Modifying
    @Query("update ArticleDO a set a.traffic = :traffic where a.id = :id")
    void setArticleTraffic(@Param("id") Long id, @Param("traffic") Long traffic);

}
