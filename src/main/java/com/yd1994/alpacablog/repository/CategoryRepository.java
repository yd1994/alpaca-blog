package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.CategoryDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<CategoryDO, Long>, JpaSpecificationExecutor<CategoryDO> {

    /**
     * 通过 id、delete 查找
     * @param id
     * @param delete
     * @return
     */
    CategoryDO findFirstByIdAndDelete(Long id, Boolean delete);
}
