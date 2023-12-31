package com.zb.dto.store;

import com.zb.dto.user.ManagerInfoDto;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StoreDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StoreRequest {

        @Size(min = 1, max = 50)
        private String storeName;
        private String location;
        @Size(min = 1, max = 100)
        private String description;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Info {

        private Long id;
        private String storeName;
        private String location;
        private String description;
        private ManagerInfoDto manager;
        private double rating;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StoreResponse {

        private Info info;
    }

}
