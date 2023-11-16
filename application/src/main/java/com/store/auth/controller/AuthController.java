package com.store.auth.controller;

import com.store.auth.jwt.JwtFilter;
import com.store.auth.service.AuthService;
import com.store.dto.AuthDto;
import com.store.dto.AuthDto.SignUpCustomer;
import com.store.dto.AuthDto.SignUpManager;
import com.store.dto.TokenDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/signup-customer")
    public ResponseEntity<SignUpCustomer> signUpCustomer(
      @Valid @RequestBody AuthDto.SignUpCustomer form) {
        return ResponseEntity.ok(authService.signUpCustomer(form));
    }

    @PostMapping("/signup-manager")
    public ResponseEntity<SignUpManager> signUpManager(
      @Valid @RequestBody AuthDto.SignUpManager form) {
        return ResponseEntity.ok(authService.signUpManager(form));
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody AuthDto.SignIn form) {
        TokenDto tokenDto = authService.signIn(form);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.AUTHORIZATION_HEADER_PREFIX + tokenDto.getToken());

        return ResponseEntity.ok().headers(httpHeaders).body(tokenDto);
    }

    @GetMapping("/signin/test")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }
}
