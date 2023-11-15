package com.store.dto;

import com.store.entity.Customer;
import com.store.entity.Manager;
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
        private String userType;
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

        private AuthorityDto authority;

        public static SignUpCustomer toEntity(Customer customer) {
            return SignUpCustomer.builder()
                                 .username(customer.getUsername())
                                 .password(customer.getPassword())
                                 .authority(
                                   AuthorityDto.builder().authorityName(customer.getAuthority().getAuthorityName())
                                               .build())
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

        private AuthorityDto authority;

        public static SignUpManager toEntity(Manager manager) {
            return SignUpManager.builder()
                                .username(manager.getUsername())
                                .password(manager.getPassword())
                                .authority(
                                  AuthorityDto.builder().authorityName(manager.getAuthority().getAuthorityName())
                                              .build())
                                .build();
        }
    }
}
