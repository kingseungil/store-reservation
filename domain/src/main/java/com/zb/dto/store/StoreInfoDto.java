package com.zb.dto.store;

import com.zb.entity.Store;
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

    private Long id;
    private String storeName;
    private String location;


    public static StoreInfoDto from(Store store) {
        return StoreInfoDto.builder()
                           .id(store.getStoreId())
                           .storeName(store.getStoreName())
                           .location(store.getLocation())
                           .build();
    }

}

