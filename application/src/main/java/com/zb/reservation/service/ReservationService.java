package com.zb.reservation.service;

import com.zb.dto.reservation.ReservationDto;

public interface ReservationService {

    void reserve(Long storeId, ReservationDto.Request form);

    void getReservationByStoreId(Long storeId);

    void cancelReservation(Long reservationId);

    void acceptReservation(Long reservationId);

    void rejectReservation(Long reservationId);

}