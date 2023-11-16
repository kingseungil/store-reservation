package com.zb.auth.controller;

import com.zb.annotation.OnlyCustomer;
import com.zb.auth.jwt.JwtFilter;
import com.zb.auth.service.AuthService;
import com.zb.dto.AuthDto;
import com.zb.dto.AuthDto.SignUpCustomer;
import com.zb.dto.AuthDto.SignUpManager;
import com.zb.dto.TokenDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<SignUpCustomer> signUpCustomer(
      @Valid @RequestBody AuthDto.SignUpCustomer form) {
        return ResponseEntity.ok(authService.signUpCustomer(form));
    }

    /**
     * 매니저(상점) 회원가입
     *
     * @param form 회원가입 폼
     * @return 회원가입 결과
     */
    @PostMapping("/signup-manager")
    public ResponseEntity<SignUpManager> signUpManager(
      @Valid @RequestBody AuthDto.SignUpManager form) {
        return ResponseEntity.ok(authService.signUpManager(form));
    }

    /**
     * 로그인
     *
     * @param form 로그인 폼
     * @return 토큰
     */
    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody AuthDto.SignIn form) {
        TokenDto tokenDto = authService.signIn(form);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.AUTHORIZATION_HEADER_PREFIX + tokenDto.getToken());

        return ResponseEntity.ok().headers(httpHeaders).body(tokenDto);
    }

    @GetMapping("/test")
    @OnlyCustomer
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }

}
