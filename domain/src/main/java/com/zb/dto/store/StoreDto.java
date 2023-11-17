package com.zb.dto.store;

import com.zb.dto.user.ManagerDto;
import com.zb.entity.Store;
import jakarta.validation.constraints.Size;
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

    @Size(min = 1, max = 50)
    private String storeName;
    private String location;
    @Size(min = 1, max = 100)
    private String description;
    private ManagerDto manager;

    // entity -> dto
    public static StoreDto toDto(Store store) {
        ManagerDto managerDto = ManagerDto.builder()
                                          .username(store.getManager().getUsername())
                                          .build();
        return StoreDto.builder()
                       .storeName(store.getStoreName())
                       .location(store.getLocation())
                       .description(store.getDescription())
                       .manager(managerDto)
                       .build();
    }

    // dto -> entity
    public static Store toEntity(StoreDto storeDto) {

        return Store.builder()
                    .storeName(storeDto.getStoreName())
                    .location(storeDto.getLocation())
                    .description(storeDto.getDescription())
                    .build();
    }

}
