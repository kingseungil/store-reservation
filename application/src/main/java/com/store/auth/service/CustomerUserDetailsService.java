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

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByUsername(username)
                                 .map(customer -> createUser(username, customer))
                                 .orElseThrow(
                                   () -> new CustomException(USER_NOT_FOUND)
                                 );
    }

    /**
     * @param username 유저 이름
     * @param customer user 엔티티
     * @return UserDetails 객체
     */
    private User createUser(String username, Customer customer) {
        if (!customer.isActivated()) {
            throw new CustomException(NOT_ACTIVATED_USER);
        }

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
          String.valueOf(customer.getAuthority().getAuthorityName()));

        return new User(customer.getUsername(), customer.getPassword(), Collections.singleton(authority));
    }
}
