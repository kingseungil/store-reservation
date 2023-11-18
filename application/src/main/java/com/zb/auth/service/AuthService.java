package com.zb.auth.service;

import com.zb.dto.auth.AuthDto.SignIn;
import com.zb.dto.auth.AuthDto.SignUpCustomer;
import com.zb.dto.auth.AuthDto.SignUpManager;

public interface AuthService {

    SignUpCustomer.Response signUpCustomer(SignUpCustomer.Request form);

    SignUpManager.Response signUpManager(SignUpManager.Request form);

    SignIn.Response signIn(SignIn.Request form);
}
