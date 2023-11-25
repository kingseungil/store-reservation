package com.zb.auth.service;

import static com.zb.type.ErrorCode.NOT_SUPPORTED_USER_TYPE;
import static com.zb.type.UserRole.ROLE_ADMIN;
import static com.zb.type.UserRole.ROLE_CUSTOMER;
import static com.zb.type.UserRole.ROLE_MANAGER;

import com.zb.exception.CustomException;
import com.zb.type.UserRole;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceSelector {

    private final Map<UserRole, UserDetailsService> userDetailsServiceMap;

    public UserDetailServiceSelector(
      CustomerUserDetailsService customerUserDetailsService,
      ManagerUserDetailsService managerUserDetailsService
    ) {
        this.userDetailsServiceMap = Map.of(
          ROLE_CUSTOMER, customerUserDetailsService,
          ROLE_MANAGER, managerUserDetailsService,
          ROLE_ADMIN, managerUserDetailsService // 일단 admin도 manager로 처리
        );
    }

    /**
     * 유저 타입에 따라 UserDetailsService를 선택
     */
    public UserDetailsService select(UserRole role) {
        UserDetailsService userDetailsService = userDetailsServiceMap.get(role);
        if (userDetailsService == null) {
            throw new CustomException(NOT_SUPPORTED_USER_TYPE);
        }
        return userDetailsService;
    }
}
