package com.zb.auth.controller;

import com.zb.auth.jwt.JwtFilter;
import com.zb.auth.service.AuthService;
import com.zb.dto.auth.AuthDto;
import com.zb.dto.auth.AuthDto.SignInResponse;
import com.zb.dto.auth.AuthDto.SignUpResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AUTH")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    /**
     * 고객 회원가입
     *
     * @param form 회원가입 폼
     * @return 회원가입 결과
     */
    @PostMapping("/signup-customer")
    public ResponseEntity<SignUpResponse> signUpCustomer(
      @Valid @RequestBody AuthDto.SignUpCustomerRequest form) {
        return ResponseEntity.ok(authService.signUpCustomer(form));
    }

    /**
     * 매니저(상점) 회원가입
     *
     * @param form 회원가입 폼
     * @return 회원가입 결과
     */
    @PostMapping("/signup-manager")
    public ResponseEntity<SignUpResponse> signUpManager(
      @Valid @RequestBody AuthDto.SignUpManagerRequest form) {
        return ResponseEntity.ok(authService.signUpManager(form));
    }

    /**
     * 로그인
     *
     * @param form 로그인 폼
     * @return 토큰
     */
    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody AuthDto.SignInRequest form) {
        SignInResponse token = authService.signIn(form);

        // 토큰을 헤더에 넣어서 반환
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.AUTHORIZATION_HEADER_PREFIX + token.getToken());

        return ResponseEntity.ok().headers(httpHeaders).body(token);
    }

}
