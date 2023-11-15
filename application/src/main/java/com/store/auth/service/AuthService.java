package com.store.auth.service;

import static com.store.type.ErrorCode.ALREADY_EXISTED_USER;
import static com.store.type.ErrorCode.NOT_SUPPORTED_USER_TYPE;
import static com.store.type.ErrorCode.UNMATCHED_PASSWORD;
import static com.store.type.UserType.CUSTOMER;
import static com.store.type.UserType.MANAGER;

import com.store.auth.jwt.JwtTokenProvider;
import com.store.dto.AuthDto.SignIn;
import com.store.dto.AuthDto.SignUpCustomer;
import com.store.dto.AuthDto.SignUpManager;
import com.store.dto.TokenDto;
import com.store.entity.Authority;
import com.store.entity.Customer;
import com.store.entity.Manager;
import com.store.exception.CustomException;
import com.store.repository.CustomerRepository;
import com.store.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final ManagerUserDetailsService managerUserDetailsService;

    @Transactional
    public SignUpCustomer signUpCustomer(SignUpCustomer form) {
        if (customerRepository.findByUsername(form.getUsername()).isPresent()) {
            throw new CustomException(ALREADY_EXISTED_USER);
        }

        Authority authority = Authority.builder()
                                       .authorityName(CUSTOMER)
                                       .build();

        Customer customer = Customer.builder()
                                    .username(form.getUsername())
                                    .password(passwordEncoder.encode(form.getPassword()))
                                    .activated(true)
                                    .authority(authority)
                                    .build();

        return SignUpCustomer.toEntity(customerRepository.save(customer));
    }

    @Transactional
    public SignUpManager signUpManager(SignUpManager form) {
        if (managerRepository.findByUsername(form.getUsername()).isPresent()) {
            throw new CustomException(ALREADY_EXISTED_USER);
        }

        Authority authority = Authority.builder()
                                       .authorityName(MANAGER)
                                       .build();

        Manager manager = Manager.builder()
                                 .username(form.getUsername())
                                 .password(passwordEncoder.encode(form.getPassword()))
                                 .activated(true)
                                 .authority(authority)
                                 .build();

        return SignUpManager.toEntity(managerRepository.save(manager));
    }

    public TokenDto signIn(SignIn form) {
        UserDetails userDetails = getUserDetails(form);

        // Check the password
        if (!passwordEncoder.matches(form.getPassword(), userDetails.getPassword())) {
            throw new CustomException(UNMATCHED_PASSWORD);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(authentication);

        return TokenDto.builder()
                       .token(jwt)
                       .build();
    }

    private UserDetails getUserDetails(SignIn form) {
        UserDetailsService userDetailsService;
        if (CUSTOMER.equals(form.getUserType())) {
            userDetailsService = customerUserDetailsService;
        } else if (MANAGER.equals(form.getUserType())) {
            userDetailsService = managerUserDetailsService;
        } else {
            throw new CustomException(NOT_SUPPORTED_USER_TYPE);
        }

        return userDetailsService.loadUserByUsername(form.getUsername());
    }
}
