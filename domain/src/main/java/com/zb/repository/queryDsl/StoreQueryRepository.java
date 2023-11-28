package com.zb.repository.queryDsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.dto.store.StoreDto.StoreRequest;
import com.zb.entity.Manager;
import com.zb.entity.QManager;
import com.zb.entity.QReview;
import com.zb.entity.QStore;
import com.zb.entity.Store;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QStore qStore = QStore.store;
    private final QManager qManager = QManager.manager;
    private final QReview qReview = QReview.review;

    public boolean existsByStoreName(String storeName) {
        Integer fetchOne = queryFactory.selectOne()
                                       .from(qStore)
                                       .where(eqStoreName(storeName))
                                       .fetchFirst();

        return fetchOne != null;
    }

    private BooleanExpression eqStoreName(String storeName) {
        if (storeName != null) {
            return qStore.storeName.eq(storeName);
        }

        return null;
    }

    public Optional<Store> findById(Long storeId) {
        return Optional.ofNullable(queryFactory.select(Projections.fields(Store.class,
                                                 Expressions.asNumber(storeId).as("id"),
                                                 qStore.storeName,
                                                 qStore.description,
                                                 Projections.fields(Manager.class,
                                                   qManager.managerId,
                                                   qManager.username
                                                 ).as("manager")
                                               ))
                                               .from(qStore)
                                               .innerJoin(qStore.manager)
                                               .where(eqStoreId(storeId))
                                               .fetchOne());
    }

    public void updateStore(StoreRequest form, Long storeId) {
        queryFactory.update(qStore)
                    .set(qStore.storeName, form.getStoreName())
                    .set(qStore.description, form.getDescription())
                    .set(qStore.location, form.getLocation())
                    .where(eqStoreId(storeId))
                    .execute();
    }

    public void deleteStore(Long storeId) {
        queryFactory.delete(qStore)
                    .where(eqStoreId(storeId))
                    .execute();
    }

    private BooleanExpression eqStoreId(Long storeId) {
        if (storeId != null) {
            return qStore.storeId.eq(storeId);
        }

        return null;
    }

    public Slice<Tuple> findAllWithAverageRating(Pageable pageable) {
        List<Tuple> stores = queryFactory.select(Projections.fields(Store.class,
                                             qStore.storeId,
                                             qStore.storeName,
                                             qStore.location,
                                             qStore.description,
                                             Projections.fields(Manager.class,
                                               qManager.managerId,
                                               qManager.username
                                             ).as("manager")),
                                           qReview.rating.avg().as("averageRating"))
                                         .from(qStore)
                                         .leftJoin(qStore.manager, qManager)
                                         .leftJoin(qStore.reviews, qReview)
                                         .groupBy(qStore.storeId)
                                         .orderBy(qReview.rating.avg().desc())
                                         .offset(pageable.getOffset())
                                         .limit(pageable.getPageSize() + 1)
                                         .fetch();

        boolean hasNext = false;
        if (stores.size() > pageable.getPageSize()) {
            hasNext = true;
            stores.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(stores, pageable, hasNext);
    }

    public Optional<Tuple> findByIdWithAverageRating(Long storeId) {
        return Optional.ofNullable(queryFactory.select(Projections.fields(Store.class,
                                                   qStore.storeId,
                                                   qStore.storeName,
                                                   qStore.location,
                                                   qStore.description,
                                                   Projections.fields(Manager.class,
                                                     qManager.managerId,
                                                     qManager.username
                                                   ).as("manager")),
                                                 qReview.rating.avg().as("averageRating"))
                                               .from(qStore)
                                               .leftJoin(qStore.manager, qManager)
                                               .leftJoin(qStore.reviews, qReview)
                                               .where(eqStoreId(storeId))
                                               .groupBy(qStore.storeId)
                                               .fetchOne());
    }
}
