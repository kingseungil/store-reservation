package com.zb.dto.store;

import com.zb.dto.user.ManagerDto;
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
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private Info info;
    }

}
