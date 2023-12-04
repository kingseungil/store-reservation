package com.zb.repository.queryDsl;

import static com.zb.type.ErrorCode.NOT_RESERVATION_OWNER;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.zb.entity.Customer;
import com.zb.entity.QCustomer;
import com.zb.entity.QReservation;
import com.zb.entity.QStore;
import com.zb.entity.Reservation;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.type.ReservationStatus;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QReservation qReservation = QReservation.reservation;
    private final QCustomer qCustomer = QCustomer.customer;
    private final QStore qStore = QStore.store;

    public void updateReservationStatus(Long reservationId, String username, ReservationStatus status) {
        JPAUpdateClause updateClause = queryFactory.update(qReservation);
        long affectedRows = updateClause.set(Collections.singletonList(qReservation.status),
                                          Collections.singletonList(status))
                                        .where(eqReservationId(reservationId))
                                        .where(eqCustomerUsername(username))
                                        .execute();

        if (affectedRows != 1) {
            throw new CustomException(NOT_RESERVATION_OWNER);
        }
    }

    private BooleanExpression eqCustomerUsername(String username) {
        if (username != null) {
            return qReservation.customer.username.eq(username);
        }

        return null;
    }

    public void updateReservationStatusForManager(Long reservationId, String username, ReservationStatus status) {
        JPAUpdateClause updateClause = queryFactory.update(qReservation);
        long affectedRows = updateClause.set(Collections.singletonList(qReservation.status),
                                          Collections.singletonList(status))
                                        .where(eqReservationId(reservationId))
                                        .where(eqManagerUsername(username))
                                        .execute();

        if (affectedRows != 1) {
            throw new CustomException(NOT_RESERVATION_OWNER);
        }
    }

    private BooleanExpression eqManagerUsername(String username) {
        if (username != null) {
            return qReservation.store.manager.username.eq(username);
        }

        return null;
    }

    public Optional<Reservation> findById(Long reservationId) {
        return Optional.ofNullable(queryFactory.select(Projections.fields(Reservation.class,
                                                 Expressions.asNumber(reservationId).as("id"),
                                                 qReservation.reservationDate,
                                                 qReservation.status,
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
                                               .from(qReservation)
                                               .innerJoin(qReservation.customer, qCustomer)
                                               .innerJoin(qReservation.store, qStore)
                                               .where(eqReservationId(reservationId))
                                               .fetchOne());
    }

    private BooleanExpression eqReservationId(Long reservationId) {
        if (reservationId != null) {
            return qReservation.id.eq(reservationId);
        }

        return null;
    }

    public List<Reservation> findByStoreIdOrderByReservationDateAsc(Long storeId) {
        return queryFactory.select(Projections.fields(Reservation.class,
                             qReservation.id,
                             qReservation.reservationDate,
                             qReservation.status,
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
                           .from(qReservation)
                           .innerJoin(qReservation.customer, qCustomer)
                           .innerJoin(qReservation.store, qStore)
                           .where(eqStoreId(storeId))
                           .orderBy(qReservation.reservationDate.asc())
                           .fetch();
    }

    public boolean existsByReservationDateAndStoreId(LocalDateTime reservationDate, Long storeId) {
        Integer fetchOne = queryFactory.selectOne()
                                       .from(qReservation)
                                       .where(eqStoreId(storeId), eqReservationDate(reservationDate))
                                       .fetchFirst();

        return fetchOne != null;
    }

    private BooleanExpression eqStoreId(Long storeId) {
        if (storeId != null) {
            return qReservation.store.id.eq(storeId);
        }

        return null;
    }

    private BooleanExpression eqReservationDate(LocalDateTime reservationDate) {
        if (reservationDate != null) {
            return qReservation.reservationDate.eq(reservationDate);
        }

        return null;
    }
}
