package com.bigbang.haruharu.repository.user;

import com.bigbang.haruharu.domain.entity.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bigbang.haruharu.domain.entity.user.QUser.user;


@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public UserRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    public List<User> findAllWithNonDefaultDailyCount() {
        return queryFactory.selectFrom(user)
                .where(user.dailyCount.ne(5))
                .fetch();
    }
}
