package com.zb.dto.store;

import com.zb.dto.user.ManagerDto;
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
public class StoreDto {

    private String storeName;
    private String location;
    private String description;
    private ManagerDto manager;

    public static StoreDto from(Store store) {
        ManagerDto managerDto = ManagerDto.builder()
                                          .id(store.getManager().getManagerId())
                                          .username(store.getManager().getUsername())
                                          .build();
        return StoreDto.builder()
                       .storeName(store.getStoreName())
                       .location(store.getLocation())
                       .description(store.getDescription())
                       .manager(managerDto)
                       .build();
    }

    public static Store to(StoreDto storeDto) {

        return Store.builder()
                    .storeName(storeDto.getStoreName())
                    .location(storeDto.getLocation())
                    .description(storeDto.getDescription())
                    .manager(ManagerDto.to(storeDto.getManager()))
                    .build();
    }

}
