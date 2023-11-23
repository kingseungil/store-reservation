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
            // 모두 허용
            .requestMatchers(
              "/api/signin", // 로그인
              "/api/signup-customer", // 고객 회원가입
              "/api/signup-manager", // 매니저 회원가입
              "/api/store/detail/{storeId}", // 상점 목록 조회
              "/api/review/list/{storeId}", // 리뷰 목록 조회
              "/api/review/detail/{reviewId}", // 리뷰 상세 조회
              "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**" // swagger
            ).permitAll()
            // 고객만 접근 가능
            .requestMatchers("/api/reservation/customer/**").hasRole("CUSTOMER") // 예약 관련
            .requestMatchers(HttpMethod.POST, "/api/review/{reservationId}").hasRole("CUSTOMER") // 리뷰 작성
            .requestMatchers(HttpMethod.PUT, "/api/review/{reservationId}").hasRole("CUSTOMER") // 리뷰 수정
            .requestMatchers(HttpMethod.DELETE, "/api/review/{reservationId}")
            .hasAnyRole("CUSTOMER", "ADMIN") // 리뷰 삭제 (관리자도 가능)
            // 매니저만 접근 가능
            .requestMatchers(
              "/api/store/manager/**", // 상점 관련
              "/api/reservation/manager/**" // 예약 관련
            ).hasRole("MANAGER")
            // 그 외 인증 없이 접근X
            .anyRequest().authenticated())

          // JwtFilter 추가
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
