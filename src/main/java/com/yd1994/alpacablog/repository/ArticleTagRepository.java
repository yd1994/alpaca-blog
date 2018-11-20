package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.ArticleTagDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yd
 */
public interface ArticleTagRepository extends JpaRepository<ArticleTagDO, Long>, JpaSpecificationExecutor<ArticleTagDO> {


    ArticleTagDO findFirstByIdAndDelete(Long id, Boolean delete);

}
