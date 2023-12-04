package com.zb.common;

import com.zb.auth.jwt.JwtTokenProvider;
import com.zb.auth.service.AuthServiceImpl;
import com.zb.auth.service.UserDetailServiceSelector;
import com.zb.repository.CustomerRepository;
import com.zb.repository.ManagerRepository;
import com.zb.repository.queryDsl.CustomerQueryRepository;
import com.zb.repository.queryDsl.ManagerQueryRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    public CustomerQueryRepository customerQueryRepository;

    @Mock
    public CustomerRepository customerRepository;

    @Mock
    public ManagerQueryRepository managerQueryRepository;

    @Mock
    public ManagerRepository managerRepository;

    @Mock
    public PasswordEncoder passwordEncoder;

    @Mock
    public JwtTokenProvider jwtTokenProvider;

    @Mock
    public UserDetailServiceSelector userDetailServiceSelector;

    @Mock
    public UserDetailsService userDetailsService;

    @Mock
    public org.springframework.security.core.userdetails.UserDetails UserDetails;

    @InjectMocks
    public AuthServiceImpl authService;
}
