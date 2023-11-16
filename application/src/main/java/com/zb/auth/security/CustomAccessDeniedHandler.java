package com.zb.auth.security;

import static com.zb.type.ErrorCode.ACCESS_DENIED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 시큐리티 단에서 발생하는 AccessDeniedException 예외 처리
    // CustomException에서 처리해주고 싶었는데, AccessDeniedException은 FilterChain에서 발생하는 예외라서 CustomException에서 처리할 수 없었음
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .errorCode(ACCESS_DENIED)
                                                   .errorMessage(ACCESS_DENIED.getDescription())
                                                   .build();

        log.error("{} is occurred in CustomAccessDeniedHandler", errorResponse.getErrorCode());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}