package com.zb.auth.security;

import com.zb.auth.service.CustomerUserDetailsService;
import com.zb.auth.service.ManagerUserDetailsService;
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

    // AuthenticationManagerBuilder를 통해 인증을 진행할 UserDetailsService와 PasswordEncoder를 설정
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
