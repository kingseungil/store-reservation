package com.zb.auth.controller;

import com.zb.auth.jwt.JwtFilter;
import com.zb.auth.service.AuthService;
import com.zb.dto.auth.AuthDto;
import com.zb.dto.auth.AuthDto.SignIn.SignInResponse;
import com.zb.dto.auth.AuthDto.SignUpManager.SignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
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
     * @param form 회원가입 폼
     * @return 회원가입 결과
     */
    @PostMapping("/signup-customer")
    @Operation(summary = "고객 회원가입")
    public ResponseEntity<AuthDto.SignUpCustomer.SignUpResponse> signUpCustomer(
      @Valid @RequestBody AuthDto.SignUpCustomer.SignUpRequest form) {
        return ResponseEntity.ok(authService.signUpCustomer(form));
    }

    /**
     * 매니저(상점) 회원가입
     * @param form 회원가입 폼
     * @return 회원가입 결과
     */
    @PostMapping("/signup-manager")
    @Operation(summary = "매니저(상점) 회원가입")
    public ResponseEntity<SignUpResponse> signUpManager(
      @Valid @RequestBody AuthDto.SignUpManager.SignUpRequest form) {
        return ResponseEntity.ok(authService.signUpManager(form));
    }

    /**
     * 로그인
     * @param form 로그인 폼
     * @return 토큰
     */
    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "고객/매니저 선택")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody AuthDto.SignIn.SignInRequest form) {
        SignInResponse token = authService.signIn(form);

        // 토큰을 헤더에 넣어서 반환
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.AUTHORIZATION_HEADER_PREFIX + token.getToken());

        return ResponseEntity.ok().headers(httpHeaders).body(token);
    }

}
