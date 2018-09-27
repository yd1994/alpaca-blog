package com.yd1994.alpacablog.repository;

import com.yd1994.alpacablog.entity.ArticleDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleDO, Long> {
}
