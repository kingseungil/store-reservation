package com.zb.reservation.service;

import com.zb.dto.reservation.ReservationDto;
import java.util.List;

public interface ReservationService {

    void reserve(Long storeId, ReservationDto.Request form);

    List<ReservationDto.Response> getReservationsByStoreId(Long storeId);

    ReservationDto.Response getReservationByReservationId(Long reservationId);

    void cancelReservation(Long reservationId);

    void acceptReservation(Long reservationId);

    void rejectReservation(Long reservationId);

}
