package com.store.auth.service;

import static com.store.type.ErrorCode.NOT_ACTIVATED_USER;
import static com.store.type.ErrorCode.USER_NOT_FOUND;

import com.store.entity.Customer;
import com.store.exception.CustomException;
import com.store.repository.CustomerRepository;
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

    private final CustomerRepository customerRepository;

    /**
     * 주어진 사용자 이름에 해당하는 사용자의 인증 정보를 로드합니다. 사용자 이름에 해당하는 사용자가 존재하지 않는 경우, CustomException이 발생합니다.
     *
     * @param username 사용자 이름
     * @return UserDetails 사용자 인증 정보
     * @throws CustomException 사용자가 존재하지 않을 경우 발생
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByUsername(username)
                                 .map(this::createUser)
                                 .orElseThrow(
                                   () -> new CustomException(USER_NOT_FOUND)
                                 );
    }

    /**
     * Customer 엔티티를 UserDetails로 변환합니다. 사용자가 활성화되지 않은 경우, CustomException이 발생합니다.
     *
     * @param customer Customer 엔티티
     * @return User UserDetails 인스턴스
     * @throws CustomException 사용자가 활성화되지 않은 경우 발생
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
