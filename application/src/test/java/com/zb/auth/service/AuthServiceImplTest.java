package com.zb.auth.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_USER;
import static com.zb.type.ErrorCode.UNMATCHED_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zb.auth.jwt.JwtTokenProvider;
import com.zb.dto.auth.AuthDto.SignIn.SignInRequest;
import com.zb.dto.auth.AuthDto.SignIn.SignInResponse;
import com.zb.dto.auth.AuthDto.SignUpCustomer.SignUpRequest;
import com.zb.dto.auth.AuthDto.SignUpCustomer.SignUpResponse;
import com.zb.dto.auth.AuthDto.SignUpManager;
import com.zb.entity.Customer;
import com.zb.entity.Manager;
import com.zb.exception.CustomException;
import com.zb.repository.CustomerRepository;
import com.zb.repository.ManagerRepository;
import com.zb.repository.queryDsl.CustomerQueryRepository;
import com.zb.repository.queryDsl.ManagerQueryRepository;
import com.zb.type.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    CustomerQueryRepository customerQueryRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ManagerQueryRepository managerQueryRepository;

    @Mock
    ManagerRepository managerRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    UserDetailServiceSelector userDetailServiceSelector;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    UserDetails UserDetails;

    @InjectMocks
    AuthServiceImpl authService;

    @Nested
    class SignUp {

        @Test
        @DisplayName("[성공]고객 회원가입")
        void signUpCustomer_success() {

            // given
            SignUpRequest form = new SignUpRequest("username", "password", UserRole.ROLE_CUSTOMER, "010-1234-1234");

            // when
            when(customerQueryRepository.existsByUsername(form.getUsername())).thenReturn(false);
            when(passwordEncoder.encode(form.getPassword())).thenReturn(anyString());

            SignUpResponse result = authService.signUpCustomer(form);

            // then
            verify(customerQueryRepository, times(1)).existsByUsername(form.getUsername());
            verify(passwordEncoder, times(1)).encode(form.getPassword());
            verify(customerRepository, times(1)).save(any(Customer.class));

            assertThat(result.getUsername()).isEqualTo(form.getUsername());
        }

        @Test
        @DisplayName("[실패]고객 회원가입 - 이미 존재하는 유저")
        void signUpCustomer_fail_already_existed_user() {
            // given
            SignUpRequest form = new SignUpRequest("username", "password", UserRole.ROLE_CUSTOMER, "010-1234-1234");

            // when
            when(customerQueryRepository.existsByUsername(form.getUsername())).thenReturn(true);

            // then
            assertThatThrownBy(() -> authService.signUpCustomer(form))
              .isInstanceOf(CustomException.class)
              .hasMessage(ALREADY_EXISTED_USER.getMessage());
            verify(customerQueryRepository, times(1)).existsByUsername(form.getUsername());
        }

        @Test
        @DisplayName("[성공]매니저 회원가입")
        void signUpManager_success() {
            // given
            SignUpManager.SignUpRequest form = new SignUpManager.SignUpRequest("username", "password",
              UserRole.ROLE_MANAGER, "010-1234-1234");

            // when
            when(managerQueryRepository.existsByUsername(form.getUsername())).thenReturn(false);
            when(passwordEncoder.encode(form.getPassword())).thenReturn(anyString());

            SignUpManager.SignUpResponse result = authService.signUpManager(form);

            // then
            verify(managerQueryRepository, times(1)).existsByUsername(form.getUsername());
            verify(passwordEncoder, times(1)).encode(form.getPassword());
            verify(managerRepository, times(1)).save(any(Manager.class));

            assertThat(result.getUsername()).isEqualTo(form.getUsername());
        }

        @Test
        @DisplayName("[실패]매니저 회원가입 - 이미 존재하는 유저")
        void signUpManager_fail_already_existed_user() {
            // given
            SignUpManager.SignUpRequest form = new SignUpManager.SignUpRequest("username", "password",
              UserRole.ROLE_MANAGER, "010-1234-1234");

            // when
            when(managerQueryRepository.existsByUsername(form.getUsername())).thenReturn(true);

            // then
            assertThatThrownBy(() -> authService.signUpManager(form))
              .isInstanceOf(CustomException.class)
              .hasMessage(ALREADY_EXISTED_USER.getMessage());
            verify(managerQueryRepository, times(1)).existsByUsername(form.getUsername());
            verify(managerRepository, never()).save(any(Manager.class));
        }
    }

    @Nested
    class SignIn {

        @Test
        @DisplayName("[성공]고객 로그인")
        void signIn_Customer() {
            // given
            SignInRequest form = new SignInRequest("customer", "password", UserRole.ROLE_CUSTOMER);

            // when
            when(userDetailServiceSelector.select(form.getUserRole())).thenReturn(userDetailsService);
            when(userDetailsService.loadUserByUsername(form.getUsername())).thenReturn(UserDetails);
            when(passwordEncoder.matches(form.getPassword(), UserDetails.getPassword())).thenReturn(true);
            when(jwtTokenProvider.createToken(any())).thenReturn(anyString());

            SignInResponse result = authService.signIn(form);

            // then
            verify(userDetailServiceSelector, times(1)).select(form.getUserRole());
            verify(userDetailsService, times(1)).loadUserByUsername(form.getUsername());
            verify(passwordEncoder, times(1)).matches(form.getPassword(), UserDetails.getPassword());
            verify(jwtTokenProvider, times(1)).createToken(any());

            assertThat(result.getToken()).isNotNull();
        }

        @Test
        @DisplayName("[실패]고객 로그인 - 비밀번호 불일치")
        void signIn_Customer_fail_unmatched_password() {
            // given
            SignInRequest form = new SignInRequest("customer", "password", UserRole.ROLE_CUSTOMER);

            // when
            when(userDetailServiceSelector.select(form.getUserRole())).thenReturn(userDetailsService);
            when(userDetailsService.loadUserByUsername(form.getUsername())).thenReturn(UserDetails);
            when(passwordEncoder.matches(form.getPassword(), UserDetails.getPassword())).thenReturn(false);

            // then
            assertThatThrownBy(() -> authService.signIn(form))
              .isInstanceOf(CustomException.class)
              .hasMessage(UNMATCHED_PASSWORD.getMessage());
            verify(userDetailServiceSelector, times(1)).select(form.getUserRole());
            verify(userDetailsService, times(1)).loadUserByUsername(form.getUsername());
            verify(passwordEncoder, times(1)).matches(form.getPassword(), UserDetails.getPassword());
            verify(jwtTokenProvider, never()).createToken(any());
        }
    }
}