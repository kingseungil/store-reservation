package com.zb.repository.queryDsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.zb.dto.review.ReviewDto.ReviewRequest;
import com.zb.entity.Customer;
import com.zb.entity.QCustomer;
import com.zb.entity.QReview;
import com.zb.entity.QStore;
import com.zb.entity.Review;
import com.zb.entity.Store;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QReview qReview = QReview.review;
    private final QCustomer qCustomer = QCustomer.customer;
    private final QStore qStore = QStore.store;

    public boolean existsByCustomerAndReservation(Long customerId, Long reservationId) {
        Integer fetchOne = queryFactory.selectOne()
                                       .from(qReview)
                                       .where(
                                         eqCustomerId(customerId),
                                         eqReservationId(reservationId))
                                       .fetchFirst();

        return fetchOne != null;
    }

    private BooleanExpression eqCustomerId(Long customerId) {
        if (customerId != null) {
            return qReview.customer.id.eq(customerId);
        }

        return null;
    }

    private BooleanExpression eqReservationId(Long reservationId) {
        if (reservationId != null) {
            return qReview.reservation.id.eq(reservationId);
        }

        return null;
    }

    public Optional<Review> findById(Long reviewId) {
        return Optional.ofNullable(queryFactory.select(Projections.fields(Review.class,
                                                 Expressions.asNumber(reviewId).as("id"),
                                                 qReview.content,
                                                 qReview.rating,
                                                 qReview.createdAt,
                                                 Projections.fields(Customer.class,
                                                   qCustomer.id,
                                                   qCustomer.username,
                                                   qCustomer.phoneNumber
                                                 ).as("customer"),
                                                 Projections.fields(Store.class,
                                                   qStore.id,
                                                   qStore.storeName,
                                                   qStore.location
                                                 ).as("store")
                                               ))
                                               .from(qReview)
                                               .innerJoin(qReview.customer, qCustomer)
                                               .innerJoin(qReview.store, qStore)
                                               .where(eqReviewId(reviewId))
                                               .fetchOne());

    }

    public void updateReview(Long reviewId, ReviewRequest form) {
        JPAUpdateClause updateClause = queryFactory.update(qReview);
        updateClause.set(qReview.content, form.getContent())
                    .set(qReview.rating, form.getRating())
                    .where(eqReviewId(reviewId))
                    .execute();
    }

    public void deleteReview(Long reviewId) {
        JPADeleteClause deleteClause = queryFactory.delete(qReview);
        deleteClause.where(eqReviewId(reviewId))
                    .execute();
    }

    private BooleanExpression eqReviewId(Long reviewId) {
        if (reviewId != null) {
            return qReview.id.eq(reviewId);
        }

        return null;
    }

    public List<Review> findAllByStoreIdOrderByCreatedAtDesc(Long storeId) {
        return queryFactory.select(Projections.fields(Review.class,
                             qReview.id,
                             qReview.content,
                             qReview.rating,
                             qReview.createdAt,
                             Projections.fields(Customer.class,
                               qCustomer.id,
                               qCustomer.username,
                               qCustomer.phoneNumber
                             ).as("customer"),
                             Projections.fields(Store.class,
                               Expressions.asNumber(storeId).as("id"),
                               qStore.storeName,
                               qStore.location
                             ).as("store")
                           ))
                           .from(qReview)
                           .innerJoin(qReview.customer, qCustomer)
                           .innerJoin(qReview.store, qStore)
                           .where(eqStoreId(storeId))
                           .orderBy(qReview.createdAt.desc())
                           .fetch();
    }

    private BooleanExpression eqStoreId(Long storeId) {
        if (storeId != null) {
            return qReview.store.id.eq(storeId);
        }

        return null;
    }
}
