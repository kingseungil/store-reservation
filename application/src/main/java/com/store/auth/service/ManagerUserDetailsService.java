package com.store.auth.service;

import static com.store.type.ErrorCode.NOT_ACTIVATED_USER;
import static com.store.type.ErrorCode.USER_NOT_FOUND;

import com.store.entity.Manager;
import com.store.exception.CustomException;
import com.store.repository.ManagerRepository;
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
public class ManagerUserDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return managerRepository.findByUsername(username)
                                .map(this::createUser)
                                .orElseThrow(
                                  () -> new CustomException(USER_NOT_FOUND)
                                );
    }

    /**
     * @param manager manager 엔티티
     * @return UserDetails 객체
     */
    private User createUser(Manager manager) {
        if (!manager.isActivated()) {
            throw new CustomException(NOT_ACTIVATED_USER);
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
          String.valueOf(manager.getAuthority().getAuthorityName()));

        return new User(manager.getUsername(), manager.getPassword(), Collections.singleton(authority));
    }
}
