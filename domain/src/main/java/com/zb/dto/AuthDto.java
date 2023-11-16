package com.zb.dto;

import static com.zb.type.UserRole.ROLE_CUSTOMER;
import static com.zb.type.UserRole.ROLE_MANAGER;

import com.zb.entity.Customer;
import com.zb.entity.Manager;
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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignIn {

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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpCustomer {

        @NotNull
        @Size(min = 3, max = 50)
        private String username;

        @NotNull
        @Size(min = 3, max = 100)
        private String password;

        private UserRole authority;

        public static SignUpCustomer toEntity(Customer customer) {
            return SignUpCustomer.builder()
                                 .username(customer.getUsername())
                                 .password(customer.getPassword())
                                 .authority(ROLE_CUSTOMER)
                                 .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpManager {

        @NotNull
        @Size(min = 3, max = 50)
        private String username;

        @NotNull
        @Size(min = 3, max = 100)
        private String password;

        private UserRole authority;

        public static SignUpManager toEntity(Manager manager) {
            return SignUpManager.builder()
                                .username(manager.getUsername())
                                .password(manager.getPassword())
                                .authority(ROLE_MANAGER)
                                .build();
        }
    }
}
