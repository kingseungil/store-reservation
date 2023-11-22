package com.zb.auth.service;

import com.zb.dto.auth.AuthDto.SignIn;
import com.zb.dto.auth.AuthDto.SignIn.SignInRequest;
import com.zb.dto.auth.AuthDto.SignUpCustomer;
import com.zb.dto.auth.AuthDto.SignUpCustomer.SignUpResponse;
import com.zb.dto.auth.AuthDto.SignUpManager;
import com.zb.dto.auth.AuthDto.SignUpManager.SignUpRequest;

public interface AuthService {

    SignUpResponse signUpCustomer(SignUpCustomer.SignUpRequest form);

    SignUpManager.SignUpResponse signUpManager(SignUpRequest form);

    SignIn.SignInResponse signIn(SignInRequest form);
}
