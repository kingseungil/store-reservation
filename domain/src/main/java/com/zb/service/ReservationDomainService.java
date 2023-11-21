package com.zb.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_RESERVATION;

import com.zb.entity.Reservation;
import com.zb.exception.CustomException;
import com.zb.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationDomainService {

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public Reservation getReservationForReivew(Long reservationId, Long customerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                                                       .orElseThrow(() -> new CustomException(NOT_EXISTED_RESERVATION));
        reservation.checkStatusForReview();
        reservation.checkReservationOwner(customerId);
        return reservation;
    }
}
