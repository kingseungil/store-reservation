package com.zb.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.entity.Manager;
import com.zb.entity.QManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class ManagerQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QManager manager = QManager.manager;

    public Optional<Manager> findByUsername(String username) {
        return Optional.ofNullable(queryFactory.select(manager)
                                               .from(manager)
                                               .where(eqUsername(username))
                                               .fetchOne());
    }

    public boolean existsByUsername(String username) {
        Integer fetchOne = queryFactory.selectOne()
                                       .from(manager)
                                       .where(eqUsername(username))
                                       .fetchFirst();

        return fetchOne != null;
    }

    private BooleanExpression eqUsername(String username) {
        if (StringUtils.hasText(username)) {
            return manager.username.eq(username);
        }

        return null;
    }
}
