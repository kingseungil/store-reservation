package com.zb.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zb.dto.store.StoreInfoDto;
import com.zb.dto.user.CustomerInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReservationRequest {

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(description = "예약 날짜", example = "2023-11-22T12:00")
        private LocalDateTime reservationDate;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Info {

        private LocalDateTime reservationDate;
        private CustomerInfoDto customer;
        private StoreInfoDto store;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReservationResponse {

        private ReservationDto.Info info;
    }

}
