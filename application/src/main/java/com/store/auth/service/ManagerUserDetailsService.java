package com.store.auth.service;

import com.store.entity.Manager;
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
                                .map(manager -> createUser(username, manager))
                                // TODO : Custom Exception
                                .orElseThrow(
                                  () -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    /**
     * @param username 유저 이름
     * @param manager  manager 엔티티
     * @return UserDetails 객체
     */
    private User createUser(String username, Manager manager) {
        if (!manager.isActivated()) {
            // TODO : Custom Exception
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
          String.valueOf(manager.getAuthority().getAuthorityName()));

        return new User(manager.getUsername(), manager.getPassword(), Collections.singleton(authority));
    }
}
