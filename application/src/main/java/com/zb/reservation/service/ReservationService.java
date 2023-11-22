package com.zb.reservation.service;

import com.zb.dto.reservation.ReservationDto.ReservationRequest;
import com.zb.dto.reservation.ReservationDto.ReservationResponse;
import java.util.List;

public interface ReservationService {

    void reserve(Long storeId, ReservationRequest form);

    List<ReservationResponse> getReservationsByStoreId(Long storeId);

    ReservationResponse getReservationByReservationId(Long reservationId);

    void cancelReservation(Long reservationId);

    void acceptReservation(Long reservationId);

    void rejectReservation(Long reservationId);

    void arriveReservation(Long reservationId);

}
