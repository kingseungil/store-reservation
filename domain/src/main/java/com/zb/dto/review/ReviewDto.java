package com.zb.dto.review;

import com.zb.dto.store.StoreInfoDto;
import com.zb.dto.user.CustomerInfoDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewRequest {

        @NotNull
        private String content;
        @NotNull
        private int rating;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Info {

        private String content;
        private int rating;
        private CustomerInfoDto customer;
        private StoreInfoDto store;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewResponse {

        private ReviewDto.Info info;
    }
}
