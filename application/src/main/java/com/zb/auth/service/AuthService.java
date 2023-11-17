package com.zb.auth.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_USER;
import static com.zb.type.ErrorCode.NOT_SUPPORTED_USER_TYPE;
import static com.zb.type.ErrorCode.UNMATCHED_PASSWORD;
import static com.zb.type.UserRole.ROLE_CUSTOMER;
import static com.zb.type.UserRole.ROLE_MANAGER;

import com.zb.auth.jwt.JwtTokenProvider;
import com.zb.dto.auth.AuthDto.SignInRequest;
import com.zb.dto.auth.AuthDto.SignInResponse;
import com.zb.dto.auth.AuthDto.SignUpCustomerRequest;
import com.zb.dto.auth.AuthDto.SignUpManagerRequest;
import com.zb.dto.auth.AuthDto.SignUpResponse;
import com.zb.entity.Authority;
import com.zb.entity.Customer;
import com.zb.entity.Manager;
import com.zb.exception.CustomException;
import com.zb.repository.CustomerRepository;
import com.zb.repository.ManagerRepository;
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

    /**
     * 고객 회원가입
     * <p>
     * 1. 이미 존재하는지 확인
     * <p>
     * 2. 존재하지 않으면 회원가입
     *
     * @param form 회원가입 폼
     * @return 회원가입 결과
     * @throws CustomException 유저가 이미 존재하는 경우 발생
     */
    @Transactional
    public SignUpResponse signUpCustomer(SignUpCustomerRequest form) {
        if (customerRepository.findByUsername(form.getUsername()).isPresent()) {
            throw new CustomException(ALREADY_EXISTED_USER);
        }

        Authority authority = Authority.builder()
                                       .authorityName(ROLE_CUSTOMER)
                                       .build();

        Customer customer = Customer.builder()
                                    .username(form.getUsername())
                                    .password(passwordEncoder.encode(form.getPassword()))
                                    .activated(true)
                                    .authority(authority)
                                    .build();

        // 회원 저장
        customerRepository.save(customer);

        return SignUpResponse.builder()
                             .username(customer.getUsername())
                             .authority(customer.getAuthority().getAuthorityName())
                             .build();
    }

    /**
     * 매니저(상점) 회원가입
     * <p>
     * 1. 이미 존재하는지 확인
     * <p>
     * 2. 존재하지 않으면 회원가입
     *
     * @param form 회원가입 폼
     * @return 회원가입 결과
     * @throws CustomException 유저가 이미 존재하는 경우 발생
     */
    @Transactional
    public SignUpResponse signUpManager(SignUpManagerRequest form) {
        if (managerRepository.findByUsername(form.getUsername()).isPresent()) {
            throw new CustomException(ALREADY_EXISTED_USER);
        }

        Authority authority = Authority.builder()
                                       .authorityName(ROLE_MANAGER)
                                       .build();

        Manager manager = Manager.builder()
                                 .username(form.getUsername())
                                 .password(passwordEncoder.encode(form.getPassword()))
                                 .activated(true)
                                 .authority(authority)
                                 .build();

        // 회원 저장
        managerRepository.save(manager);

        return SignUpResponse.builder()
                             .username(manager.getUsername())
                             .authority(manager.getAuthority().getAuthorityName())
                             .build();
    }

    /**
     * 로그인
     * <p>
     * 1. 로그인 폼에서 받은 UserRole에 따라 UserDetailsService를 선택
     * <p>
     * 2. UserDetails에서 비밀번호를 가져와서 입력받은 비밀번호와 비교
     * <p>
     * 3. 비밀번호가 일치하면 토큰 생성
     *
     * @param form 로그인 폼
     * @return 토큰
     * @throws CustomException 비밀번호가 일치하지 않을 경우 발생
     */
    public SignInResponse signIn(SignInRequest form) {
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

        return SignInResponse.builder()
                             .token(jwt)
                             .build();
    }

    private UserDetails getUserDetails(SignInRequest form) {
        UserDetailsService userDetailsService;
        if (ROLE_CUSTOMER.equals(form.getUserRole())) {
            userDetailsService = customerUserDetailsService;
        } else if (ROLE_MANAGER.equals(form.getUserRole())) {
            userDetailsService = managerUserDetailsService;
        } else {
            throw new CustomException(NOT_SUPPORTED_USER_TYPE);
        }

        return userDetailsService.loadUserByUsername(form.getUsername());
    }
}
