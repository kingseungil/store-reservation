package com.zb.dto.auth;

import com.zb.type.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class AuthDto {

    public static class SignIn {

        @Getter
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
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class SignInResponse {

            private String token;
        }
    }

    public static class SignUpCustomer {

        @Getter
        public static class SignUpRequest {

            @NotNull
            @Size(min = 3, max = 50)
            private String username;

            @NotNull
            @Size(min = 3, max = 100)
            private String password;

            private UserRole authority;

            @NotNull
            // 핸드폰번호 유효성 검사(010-1234-5678)
            @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "핸드폰번호 형식이 맞지 않습니다.")
            private String phoneNumber;
        }


        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class SignUpResponse {

            private String username;
            private String phoneNumber;
            private UserRole authority;
        }
    }

    public static class SignUpManager {

        @Getter
        public static class SignUpRequest {

            @NotNull
            @Size(min = 3, max = 50)
            private String username;

            @NotNull
            @Size(min = 3, max = 100)
            private String password;

            private UserRole authority;

            @NotNull
            // 핸드폰번호 유효성 검사(010-1234-5678)
            @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "핸드폰번호 형식이 맞지 않습니다.")
            private String phoneNumber;
        }


        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class SignUpResponse {

            private String username;
            private String phoneNumber;
            private UserRole authority;
        }
    }
}