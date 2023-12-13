package com.bigbang.haruharu.repository.concept;

import com.bigbang.haruharu.domain.entity.base.BaseEntity;
import com.bigbang.haruharu.domain.entity.concept.Concept;
import com.bigbang.haruharu.dto.entityDto.ConceptDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bigbang.haruharu.domain.entity.concept.QConcept.*;

@Repository
public class ConceptRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public ConceptRepositorySupport(JPAQueryFactory queryFactory) {
        super(Concept.class);
        this.queryFactory = queryFactory;
    }

    public List<ConceptDto> findConceptList() {
        return queryFactory.select(
                        Projections.constructor(
                                ConceptDto.class,
                                concept.conceptSeq,
                                concept.name
                        )
                ).from(concept)
                .where(concept.deleteYn.eq(BaseEntity.YN.N))
                .fetch();
    }
}
