package com.zb.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zb.dto.store.StoreInfoDto;
import com.zb.dto.user.CustomerInfoDto;
import com.zb.type.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

        private CustomerInfoDto customer;
        private StoreInfoDto store;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReservationsResponse {

        private Long id;
        private LocalDateTime reservationDate;
        private List<ReservationTimeTable> timeTable;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReservationResponse {

        private Long id;
        private LocalDateTime reservationDate;
        private ReservationDto.Info info;

    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReservationTimeTable {

        private LocalTime time;
        private ReservationStatus status;
        private ReservationDto.Info info;
    }
}
