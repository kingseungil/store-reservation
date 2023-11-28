package com.zb.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.entity.Customer;
import com.zb.entity.QCustomer;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class CustomerQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QCustomer customer = QCustomer.customer;

    public Optional<Customer> findByUsername(String username) {
        // TODO : Projections.fields() 사용
        return Optional.ofNullable(queryFactory.select(customer)
                                               .from(customer)
                                               .where(eqUsername(username))
                                               .fetchFirst());
    }


    public boolean existsByUsername(String username) {
        Integer fetchOne = queryFactory.selectOne()
                                       .from(customer)
                                       .where(eqUsername(username))
                                       .fetchFirst();

        return fetchOne != null;
    }

    private BooleanExpression eqUsername(String username) {
        if (StringUtils.hasText(username)) {
            return customer.username.eq(username);
        }

        return null;
    }
}
