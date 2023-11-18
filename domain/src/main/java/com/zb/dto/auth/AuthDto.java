package com.zb.dto.auth;

import com.zb.type.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class AuthDto {

    public static class SignIn {

        @Getter
        public static class Request {

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
        public static class Response {

            private String token;
        }
    }

    public static class SignUpCustomer {

        @Getter
        public static class Request {

            @NotNull
            @Size(min = 3, max = 50)
            private String username;

            @NotNull
            @Size(min = 3, max = 100)
            private String password;

            private UserRole authority;
        }


        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Response {

            private String username;
            private UserRole authority;
        }
    }

    public static class SignUpManager {

        @Getter
        public static class Request {

            @NotNull
            @Size(min = 3, max = 50)
            private String username;

            @NotNull
            @Size(min = 3, max = 100)
            private String password;

            private UserRole authority;
        }


        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Response {

            private String username;
            private UserRole authority;
        }
    }
}