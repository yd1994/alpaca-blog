package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.CategoryDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDO, Long> {
}
