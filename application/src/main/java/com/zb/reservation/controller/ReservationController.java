package com.zb.reservation.controller;

import com.zb.annotation.OnlyCustomer;
import com.zb.annotation.OnlyManager;
import com.zb.dto.reservation.ReservationDto.ReservationRequest;
import com.zb.dto.reservation.ReservationDto.ReservationResponse;
import com.zb.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "RESERVATION")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    /* 손님용 */
    @PostMapping("/customer/{storeId}")
    @OnlyCustomer
    @Operation(summary = "예약하기")
    public ResponseEntity<String> reserve(
      @PathVariable Long storeId,
      @Valid @RequestBody ReservationRequest form
    ) {
        reservationService.reserve(storeId, form);
        return ResponseEntity.ok("예약 성공");
    }

    @GetMapping("/customer/{reservationId}")
    @OnlyCustomer
    @Operation(summary = "예약 조회")
    public ResponseEntity<ReservationResponse> getReservationByReservationId(
      @PathVariable Long reservationId
    ) {
        return ResponseEntity.ok(reservationService.getReservationByReservationId(reservationId));
    }

    @PatchMapping("/customer/cancel/{reservationId}")
    @OnlyCustomer
    @Operation(summary = "예약 취소")
    public ResponseEntity<String> cancelReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok("예약이 취소되었습니다.");
    }

    @PatchMapping("/customer/arrive/{reservationId}")
    @OnlyCustomer
    @Operation(summary = "도착 완료")
    public ResponseEntity<String> arriveReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.arriveReservation(reservationId);
        return ResponseEntity.ok("도착 완료되었습니다");
    }

    /* 매니저용 */
    @GetMapping("/manager/{storeId}")
    @OnlyManager
    @Operation(summary = "가게별 예약 조회(매니저)")
    public ResponseEntity<List<ReservationResponse>> getReservations(
      @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(reservationService.getReservationsByStoreId(storeId));
    }

    @PatchMapping("/manager/reject/{reservationId}")
    @OnlyManager
    @Operation(summary = "예약 거절(매니저)")
    public ResponseEntity<String> rejectReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.rejectReservation(reservationId);
        return ResponseEntity.ok("예약을 거절하였습니다.");
    }

    @PatchMapping("/manager/accept/{reservationId}")
    @OnlyManager
    @Operation(summary = "예약 수락(매니저)")
    public ResponseEntity<String> acceptReservation(
      @PathVariable Long reservationId
    ) {
        reservationService.acceptReservation(reservationId);
        return ResponseEntity.ok("예약을 수락하였습니다.");
    }


}
