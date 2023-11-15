package com.store.auth.service;

import com.store.entity.Customer;
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
                                 // TODO : Custom Exception
                                 .orElseThrow(
                                   () -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    /**
     * @param username 유저 이름
     * @param customer user 엔티티
     * @return UserDetails 객체
     */
    private User createUser(String username, Customer customer) {
        if (!customer.isActivated()) {
            // TODO : Custom Exception
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
          String.valueOf(customer.getAuthority().getAuthorityName()));

        return new User(customer.getUsername(), customer.getPassword(), Collections.singleton(authority));
    }
}
