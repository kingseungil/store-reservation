package com.zb.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_RESERVATION;
import static com.zb.type.ErrorCode.NOT_EXISTED_RESERVATION;
import static com.zb.type.ErrorCode.NOT_OWNER_STORE;
import static com.zb.type.ErrorCode.NOT_RESERVATION_OWNER;

import com.zb.entity.Reservation;
import com.zb.exception.CustomException;
import com.zb.repository.ReservationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationDomainService {

    private final ReservationRepository reservationRepository;

    /**
     * 예약 조회 (예약 ID로)
     * <p>
     * - 리뷰 작성이 가능한 상태인지 확인
     * <p>
     * - 예약자인지 확인
     */
    @Transactional(readOnly = true)
    public Reservation getReservationForReivew(Long reservationId, Long customerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                                                       .orElseThrow(() -> new CustomException(NOT_EXISTED_RESERVATION));
        reservation.checkStatusForReview();
        reservation.checkReservationOwner(customerId);
        return reservation;
    }

    /**
     * 이미 존재하는 예약인지 확인
     */
    public void checkExistReservation(Long storeId, LocalDateTime reservationDate) {
        if (reservationRepository.existsByReservationDateAndStoreId(reservationDate, storeId)) {
            throw new CustomException(ALREADY_EXISTED_RESERVATION);
        }
    }

    /**
     * 예약자인지 확인
     */
    @Transactional(readOnly = true)
    public Reservation getReservationOfCustomer(Long reservationId, String username) {
        return reservationRepository.findById(reservationId)
                                    .filter(reservation -> reservation.getCustomer()
                                                                      .getUsername()
                                                                      .equals(username))
                                    .orElseThrow(() -> new CustomException(NOT_RESERVATION_OWNER));
    }


    /**
     * 자기 매장인지 확인
     */
    @Transactional(readOnly = true)
    public Reservation getStoreOfManager(Long reservationId, String managerName) {
        return reservationRepository.findById(reservationId)
                                    .filter(reservation -> reservation.getStore()
                                                                      .getManager()
                                                                      .getUsername()
                                                                      .equals(managerName))
                                    .orElseThrow(() -> new CustomException(NOT_OWNER_STORE));
    }
}
