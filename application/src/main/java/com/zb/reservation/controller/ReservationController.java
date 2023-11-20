package com.zb.reservation.controller;

import com.zb.annotation.OnlyCustomer;
import com.zb.annotation.OnlyManager;
import com.zb.dto.reservation.ReservationDto;
import com.zb.reservation.service.ReservationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    /* 손님용 */
    @PostMapping("/customer/{storeId}")
    @OnlyCustomer
    public ResponseEntity<String> reserve(
      @PathVariable Long storeId,
      @Valid @RequestBody ReservationDto.Request form
    ) {
        reservationService.reserve(storeId, form);
        return ResponseEntity.ok("예약 성공");
    }

    @GetMapping("/customer/{reservationId}")
    @OnlyCustomer
    public ResponseEntity<ReservationDto.Response> getReservationByReservationId(
      @PathVariable Long reservationId
    ) {
        return ResponseEntity.ok(reservationService.getReservationByReservationId(reservationId));
    }

    @PatchMapping("/customer/{reservationId}/cancel")
    @OnlyCustomer
    public ResponseEntity<String> cancelReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok("예약이 취소되었습니다.");
    }

    /* 매니저용 */
    @GetMapping("/manager/{storeId}")
    @OnlyManager
    public ResponseEntity<List<ReservationDto.Response>> getReservations(
      @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(reservationService.getReservationsByStoreId(storeId));
    }

    @PatchMapping("/manager/reject/{reservationId}")
    @OnlyManager
    public ResponseEntity<String> rejectReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.rejectReservation(reservationId);
        return ResponseEntity.ok("예약을 거절하였습니다.");
    }

    @PatchMapping("/manager/accept/{reservationId}")
    @OnlyManager
    public ResponseEntity<String> acceptReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.acceptReservation(reservationId);
        return ResponseEntity.ok("예약을 수락하였습니다.");
    }


}
