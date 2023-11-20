package com.zb.dto.reservation;

import com.zb.dto.user.CustomerDto;
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
        
        private LocalDateTime reservationDate;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Info {

        private String reservationDate;
        private CustomerDto Customer;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private ReservationDto.Info info;
    }

}
