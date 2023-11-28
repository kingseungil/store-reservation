package com.zb.repository.queryDsl;

import com.querydsl.core.types.Projections;
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
        return Optional.ofNullable(queryFactory.select(Projections.fields(Customer.class,
                                                 customer.customerId,
                                                 customer.username,
                                                 customer.phoneNumber))
                                               .from(customer)
                                               .where(eqUsername(username))
                                               .fetchFirst());
    }

    public Optional<Customer> findByUsernameForSecurity(String username) {
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
