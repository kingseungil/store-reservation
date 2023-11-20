package com.zb.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zb.dto.user.CustomerDto;
import com.zb.type.ReservationStatus;
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

    public static class Request {

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime reservationDate;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Info {

        private LocalDateTime reservationDate;
        private CustomerDto customer;
        private String storeName;
        private String location;
        private ReservationStatus status;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private ReservationDto.Info info;
    }

}
