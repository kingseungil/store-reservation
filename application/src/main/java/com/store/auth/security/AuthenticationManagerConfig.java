package com.store.auth.security;

import com.store.auth.service.CustomerUserDetailsService;
import com.store.auth.service.ManagerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig {

    private final CustomerUserDetailsService customerUserDetailsService;
    private final ManagerUserDetailsService managerUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManagerBuilder customerAuthenticationManagerBuilder() throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(null);
        builder.userDetailsService(customerUserDetailsService).passwordEncoder(passwordEncoder);
        return builder;
    }

    @Bean
    public AuthenticationManagerBuilder managerAuthenticationManagerBuilder() throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(null);
        builder.userDetailsService(managerUserDetailsService).passwordEncoder(passwordEncoder);
        return builder;
    }
}
