package com.zb.dto.store;

import com.zb.dto.user.ManagerDto;
import com.zb.entity.Store;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StoreDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        @Size(min = 1, max = 50)
        private String storeName;
        private String location;
        @Size(min = 1, max = 100)
        private String description;
        private ManagerDto manager;

        // dto -> entity
        public static Store toEntity(Request request) {
            return Store.builder()
                        .storeName(request.getStoreName())
                        .location(request.getLocation())
                        .description(request.getDescription())
                        // Manager는 별도로 설정
                        .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Info {

        private String storeName;
        private String location;
        private String description;
        private ManagerDto manager;

        // entity -> dto
        public static Info fromEntity(Store store) {
            ManagerDto managerDto = ManagerDto.builder()
                                              .username(store.getManager().getUsername())
                                              .build();
            return Info.builder()
                       .storeName(store.getStoreName())
                       .location(store.getLocation())
                       .description(store.getDescription())
                       .manager(managerDto)
                       .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private Info info;
    }
    
}
