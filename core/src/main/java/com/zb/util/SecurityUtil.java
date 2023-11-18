package com.zb.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class SecurityUtil {

    /**
     * Security Context에 저장된 username을 반환합니다.
     * <p>
     * 이미 인증이 완료된 상태에서 호출해야 합니다. (Service단...)
     *
     * @return username
     */
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.error("Security Context에 인증 정보가 없습니다.");
            return null;
        }

        return authentication.getName();
    }

    /**
     * Security Context에 Authentication 객체를 저장합니다.
     *
     * @param userDetails 인증된 사용자 정보
     */
    public static Authentication authenticate(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

}
