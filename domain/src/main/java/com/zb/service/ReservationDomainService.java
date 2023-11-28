package com.zb.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_RESERVATION;
import static com.zb.type.ErrorCode.NOT_EXISTED_RESERVATION;

import com.zb.entity.Reservation;
import com.zb.exception.CustomException;
import com.zb.repository.queryDsl.ReservationQueryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationDomainService {

    private final ReservationQueryRepository reservationQueryRepository;

    /**
     * 예약 조회 (예약 ID로)
     * <p>
     * - 리뷰 작성이 가능한 상태인지 확인
     * <p>
     * - 예약자인지 확인
     */
    @Transactional(readOnly = true)
    public Reservation getReservationForReivew(Long reservationId, Long customerId) {
        Reservation reservation = reservationQueryRepository.findById(reservationId)
                                                            .orElseThrow(
                                                              () -> new CustomException(NOT_EXISTED_RESERVATION));
        reservation.checkStatusForReview();
        reservation.checkReservationOwner(customerId);
        return reservation;
    }

    /**
     * 이미 존재하는 예약인지 확인
     */
    public void checkExistReservation(Long storeId, LocalDateTime reservationDate) {
        if (reservationQueryRepository.existsByReservationDateAndStoreId(reservationDate, storeId)) {
            throw new CustomException(ALREADY_EXISTED_RESERVATION);
        }
    }

    /**
     * 예약 취소 가능한지 확인
     */
    public void checkStatusForCancel(Long reservationId) {
        Reservation reservation = reservationQueryRepository.findById(reservationId)
                                                            .orElseThrow(
                                                              () -> new CustomException(NOT_EXISTED_RESERVATION));

        reservation.checkStatusForCancel();
    }

    /**
     * 예약 도착 가능한지 확인
     */
    public void checkStatusForArrive(Long reservationId) {
        Reservation reservation = reservationQueryRepository.findById(reservationId)
                                                            .orElseThrow(
                                                              () -> new CustomException(NOT_EXISTED_RESERVATION));

        reservation.checkStatusForArrive();
    }

    /**
     * 예약 수락 가능한지 확인
     */
    public void checkStatusForAccept(Long reservationId) {
        Reservation reservation = reservationQueryRepository.findById(reservationId)
                                                            .orElseThrow(
                                                              () -> new CustomException(NOT_EXISTED_RESERVATION));

        reservation.checkStatusForAccept();
    }

    /**
     * 예약 거절 가능한지 확인
     */
    public void checkStatusForReject(Long reservationId) {
        Reservation reservation = reservationQueryRepository.findById(reservationId)
                                                            .orElseThrow(
                                                              () -> new CustomException(NOT_EXISTED_RESERVATION));

        reservation.checkStatusForReject();
    }
}