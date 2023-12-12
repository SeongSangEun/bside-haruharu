package com.bigbang.haruharu.repository.article;

import com.bigbang.haruharu.domain.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
