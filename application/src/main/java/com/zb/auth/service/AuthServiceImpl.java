package com.zb.auth.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_USER;
import static com.zb.type.ErrorCode.UNMATCHED_PASSWORD;
import static com.zb.type.UserRole.ROLE_CUSTOMER;
import static com.zb.type.UserRole.ROLE_MANAGER;

import com.zb.auth.jwt.JwtTokenProvider;
import com.zb.dto.auth.AuthDto.SignIn;
import com.zb.dto.auth.AuthDto.SignIn.SignInRequest;
import com.zb.dto.auth.AuthDto.SignUpCustomer;
import com.zb.dto.auth.AuthDto.SignUpCustomer.SignUpResponse;
import com.zb.dto.auth.AuthDto.SignUpManager;
import com.zb.dto.auth.AuthDto.SignUpManager.SignUpRequest;
import com.zb.entity.Authority;
import com.zb.entity.Customer;
import com.zb.entity.Manager;
import com.zb.exception.CustomException;
import com.zb.repository.CustomerQueryRepository;
import com.zb.repository.CustomerRepository;
import com.zb.repository.ManagerQueryRepository;
import com.zb.repository.ManagerRepository;
import com.zb.type.UserRole;
import com.zb.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final CustomerQueryRepository customerQueryRepository;
    private final ManagerRepository managerRepository;
    private final ManagerQueryRepository managerQueryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailServiceSelector userDetailServiceSelector;


    /**
     * 고객 회원가입
     * <p>
     * 1. 이미 존재하는지 확인
     * <p>
     * 2. 존재하지 않으면 회원가입
     */
    @Transactional
    @Override
    public SignUpResponse signUpCustomer(SignUpCustomer.SignUpRequest form) {
        if (customerQueryRepository.existsByUsername(form.getUsername())) {
            throw new CustomException(ALREADY_EXISTED_USER);
        }

        Authority authority = createAuthority(ROLE_CUSTOMER);

        Customer customer = Customer.builder()
                                    .username(form.getUsername())
                                    .password(passwordEncoder.encode(form.getPassword()))
                                    .phoneNumber(form.getPhoneNumber())
                                    .activated(true)
                                    .authority(authority)
                                    .build();

        // 회원 저장
        customerRepository.save(customer);

        return SignUpResponse.from(customer);
    }


    /**
     * 매니저(상점) 회원가입
     * <p>
     * 1. 이미 존재하는지 확인
     * <p>
     * 2. 존재하지 않으면 회원가입
     */
    @Transactional
    @Override
    public SignUpManager.SignUpResponse signUpManager(SignUpRequest form) {
        if (managerQueryRepository.existsByUsername(form.getUsername())) {
            throw new CustomException(ALREADY_EXISTED_USER);
        }

        Authority authority = createAuthority(ROLE_MANAGER);

        Manager manager = Manager.builder()
                                 .username(form.getUsername())
                                 .password(passwordEncoder.encode(form.getPassword()))
                                 .activated(true)
                                 .authority(authority)
                                 .build();

        // 회원 저장
        managerRepository.save(manager);

        return SignUpManager.SignUpResponse.from(manager);
    }

    private static Authority createAuthority(UserRole userRole) {
        return Authority.builder()
                        .authorityName(userRole)
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
     */
    @Override
    public SignIn.SignInResponse signIn(SignInRequest form) {
        UserDetails userDetails = getUserDetails(form);

        checkPassword(form, userDetails);

        // SecurityContextHolder에 인증 정보를 저장
        Authentication authentication = SecurityUtil.authenticate(userDetails);

        String jwt = jwtTokenProvider.createToken(authentication);

        return SignIn.SignInResponse.builder()
                                    .token(jwt)
                                    .build();
    }


    private void checkPassword(SignInRequest form, UserDetails userDetails) {
        if (!passwordEncoder.matches(form.getPassword(), userDetails.getPassword())) {
            throw new CustomException(UNMATCHED_PASSWORD);
        }
    }

    private UserDetails getUserDetails(SignInRequest form) {
        UserDetailsService userDetailsService = userDetailServiceSelector.select(form.getUserRole());
        return userDetailsService.loadUserByUsername(form.getUsername());
    }
}
