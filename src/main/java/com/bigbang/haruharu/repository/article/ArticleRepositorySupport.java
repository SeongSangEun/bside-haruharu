package com.bigbang.haruharu.repository.article;

import com.bigbang.haruharu.domain.entity.article.Article;
import com.bigbang.haruharu.domain.entity.article.QArticle;
import com.bigbang.haruharu.domain.entity.like.Like;
import com.bigbang.haruharu.domain.entity.like.QLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.bigbang.haruharu.domain.entity.article.QArticle.*;
import static com.bigbang.haruharu.domain.entity.like.QLike.*;

@Repository
public class ArticleRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositorySupport(JPAQueryFactory queryFactory) {
        super(Article.class);
        this.queryFactory = queryFactory;
    }

    public Like existLike(Long articleSeq, Long userSeq) {
        return queryFactory.selectFrom(like)
                .join(like.article, article)
                .where(article.articleSeq.eq(articleSeq),
                        like.userSeq.eq(userSeq))
                .fetchFirst();

    }
}
