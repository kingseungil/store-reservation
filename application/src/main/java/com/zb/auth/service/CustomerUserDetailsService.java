package com.zb.auth.service;

import static com.zb.type.ErrorCode.NOT_ACTIVATED_USER;
import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.entity.Customer;
import com.zb.exception.CustomException;
import com.zb.repository.queryDsl.CustomerQueryRepository;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerQueryRepository customerQueryRepository;

    /**
     * 주어진 사용자 이름에 해당하는 사용자의 인증 정보를 로드합니다. 사용자 이름에 해당하는 사용자가 존재하지 않는 경우, CustomException이 발생합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerQueryRepository.findByUsernameForSecurity(username)
                                      .map(this::createUser)
                                      .orElseThrow(
                                        () -> new CustomException(USER_NOT_FOUND)
                                      );
    }

    /**
     * Customer 엔티티를 UserDetails로 변환합니다. 사용자가 활성화되지 않은 경우, CustomException이 발생합니다.
     */
    private User createUser(Customer customer) {
        if (!customer.isActivated()) {
            throw new CustomException(NOT_ACTIVATED_USER);
        }

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
          String.valueOf(customer.getAuthority().getAuthorityName()));

        return new User(customer.getUsername(), customer.getPassword(), Collections.singleton(authority));
    }
}
