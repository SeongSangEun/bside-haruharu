package com.bigbang.haruharu.repository.article;

import com.bigbang.haruharu.domain.entity.article.Article;
import com.bigbang.haruharu.domain.entity.article.QArticle;
import com.bigbang.haruharu.domain.entity.base.BaseEntity;
import com.bigbang.haruharu.domain.entity.like.Like;
import com.bigbang.haruharu.domain.entity.like.QLike;
import com.bigbang.haruharu.dto.entityDto.ArticleDto;
import com.bigbang.haruharu.dto.entityDto.ConceptDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bigbang.haruharu.domain.entity.article.QArticle.*;
import static com.bigbang.haruharu.domain.entity.concept.QConcept.concept;
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

    public Article findByUserSeqAndArticleSeq(Long userSeq, Long articleSeq) {
        return queryFactory.selectFrom(article)
                .where(article.articleSeq.eq(articleSeq),
                        article.userSeq.eq(userSeq),
                        article.deleteYn.eq(BaseEntity.YN.N))
                .fetchFirst();
    }

    public List<ArticleDto> getMyArticles(Long userSeq) {
        return queryFactory.select(
                        Projections.constructor(
                                ArticleDto.class,
                                article.articleSeq,
                                article.subject,
                                article.originText,
                                article.convertedText,
                                article.imageUrl,
                                article.likeCount,
                                concept.name
                        )
                ).from(article)
                .join(concept).on(article.conceptSeq.eq(concept.conceptSeq))
                .where(article.deleteYn.eq(BaseEntity.YN.N),
                        article.userSeq.eq(userSeq))
                .fetch();
    }
    public List<ArticleDto> getRandomArticles() {
        return queryFactory.select(
                        Projections.constructor(
                                ArticleDto.class,
                                article.articleSeq,
                                article.subject,
                                article.originText,
                                article.convertedText,
                                article.imageUrl,
                                article.likeCount,
                                concept.name
                        )
                ).from(article)
                .join(concept).on(article.conceptSeq.eq(concept.conceptSeq))
                .where(article.deleteYn.eq(BaseEntity.YN.N))
                .fetch();
    }
}
