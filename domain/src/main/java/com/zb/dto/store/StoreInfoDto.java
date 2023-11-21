package com.zb.dto.store;

import com.zb.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreInfoDto {

    private String storeName;
    private String location;
    private ReservationStatus status;

    public static StoreInfoDto from(String storeName, String location, ReservationStatus status) {
        return StoreInfoDto.builder()
                           .storeName(storeName)
                           .location(location)
                           .status(status)
                           .build();
    }
}

