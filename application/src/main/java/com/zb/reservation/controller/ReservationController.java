package com.zb.reservation.controller;

import com.zb.annotation.OnlyCustomer;
import com.zb.annotation.OnlyManager;
import com.zb.dto.reservation.ReservationDto;
import com.zb.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{storeId}")
    @OnlyCustomer
    public void reserve(
      @PathVariable Long storeId,
      @Valid @RequestBody ReservationDto.Request form
    ) {
        reservationService.reserve(storeId, form);
    }

    @GetMapping("/{storeId}")
    @OnlyManager
    public void getReservation(
      @PathVariable Long storeId
    ) {
        reservationService.getReservationByStoreId(storeId);
    }

    @PatchMapping("/{reservationId}/reject")
    @OnlyManager
    public void rejectReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.rejectReservation(reservationId);
    }

    @PatchMapping("/{reservationId}/accept")
    @OnlyManager
    public void acceptReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.acceptReservation(reservationId);
    }

    @PatchMapping("/{reservationId}/cancel")
    @OnlyCustomer
    public void cancelReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.cancelReservation(reservationId);
    }

}
