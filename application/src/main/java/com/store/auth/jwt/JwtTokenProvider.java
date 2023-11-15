package com.store.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    public JwtTokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    /**
     * Bean이 초기화될 때 비밀 키 설정
     *
     * @throws Exception 비밀 키 생성 중 오류가 발생한 경우
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Authentication 객체의 권한 정보를 이용해서 토큰 생성
     *
     * @param authentication Authentication 객체
     * @return 생성된 토큰
     */
    public String createToken(Authentication authentication) {
        // 많은 권한을 가지고 있을 경우를 대비해서 stream 이용
        // 현재는 권한이 하나뿐이지만 나중에 권한이 여러 개가 될 수 있음
        String authorities = authentication.getAuthorities().stream()
                                           .map(GrantedAuthority::getAuthority)
                                           .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                   .setSubject(authentication.getName())
                   .claim(AUTHORITIES_KEY, authorities)
                   .signWith(key, SignatureAlgorithm.HS512)
                   .setExpiration(validity)
                   .compact();
    }

    /**
     * Authentication 객체 생성
     *
     * @param token jwt token
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
          .parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(
                                                                     claims.get(AUTHORITIES_KEY).toString().split(", "))
                                                                   .map(SimpleGrantedAuthority::new)
                                                                   .toList();

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰 유효성 검증
     *
     * @param token jwt token
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }
}
