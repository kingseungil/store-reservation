package com.zb.auth.security;

import com.zb.auth.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
          // token을 사용하므로 csrf 설정 disable
          .csrf(AbstractHttpConfigurer::disable)

          // exception handling 설정
          .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)
          )

          // 세션은 사용하지 않으므로 STATELESS로 설정
          .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

          .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers("/api/signin").permitAll() // 로그인 api
            .requestMatchers("/api/signup-customer").permitAll() // 회원가입 api
            .requestMatchers("/api/signup-manager").permitAll() // 회원가입 api
            .requestMatchers("/api/store/manager/**").hasRole("MANAGER")
            .requestMatchers("/api/store/**").permitAll()
            .requestMatchers("/api/reservation/customer/**").hasRole("CUSTOMER")
            .requestMatchers("/api/reservation/manager/**").hasRole("MANAGER")
            .requestMatchers("/api/review/list/{storeId}").permitAll()
            .requestMatchers("/api/review/detail/{reviewId}").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/review/{reservationId}").hasRole("CUSTOMER")
            .requestMatchers(HttpMethod.PUT, "/api/review/{reservationId}").hasRole("CUSTOMER")
            .requestMatchers(HttpMethod.DELETE, "/api/review/{reservationId}").hasAnyRole("CUSTOMER", "ADMIN")
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll() // swagger
            .anyRequest().authenticated()) // 그 외 인증 없이 접근X

          // JwtFilter 추가
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
