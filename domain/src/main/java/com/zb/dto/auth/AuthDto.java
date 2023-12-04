package com.zb.dto.auth;

import com.zb.entity.Customer;
import com.zb.entity.Manager;
import com.zb.type.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class AuthDto {

    public static class SignIn {

        @Getter
        @Setter
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
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class SignInResponse {

            private String token;
        }
    }

    public static class SignUpCustomer {

        @Getter
        @Setter
        @AllArgsConstructor
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

            private Long id;
            private String username;
            private String phoneNumber;

            public static SignUpResponse from(Customer customer) {
                return SignUpResponse.builder()
                                     .id(customer.getId())
                                     .username(customer.getUsername())
                                     .phoneNumber(customer.getPhoneNumber())
                                     .build();
            }
        }
    }

    public static class SignUpManager {

        @Getter
        @Setter
        @AllArgsConstructor
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

            private Long id;
            private String username;
            private String phoneNumber;

            public static SignUpResponse from(Manager manager) {
                return SignUpResponse.builder()
                                     .id(manager.getId())
                                     .username(manager.getUsername())
                                     .phoneNumber(manager.getPhoneNumber())
                                     .build();
            }
        }

    }
}