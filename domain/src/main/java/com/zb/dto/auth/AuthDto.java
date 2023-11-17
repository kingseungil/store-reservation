package com.zb.dto.auth;

import com.zb.type.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class AuthDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInRequest {

        @NotNull
        @Size(min = 3, max = 50)
        private String username;

        @NotNull
        @Size(min = 3, max = 100)
        private String password;

        @NotNull
        private UserRole userRole;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInResponse {

        @NotNull
        private String token;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpCustomerRequest {

        @NotNull
        @Size(min = 3, max = 50)
        private String username;

        @NotNull
        @Size(min = 3, max = 100)
        private String password;

        private UserRole authority;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpManagerRequest {

        @NotNull
        @Size(min = 3, max = 50)
        private String username;

        @NotNull
        @Size(min = 3, max = 100)
        private String password;

        private UserRole authority;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpResponse {

        private String username;
        private UserRole authority;

    }
}