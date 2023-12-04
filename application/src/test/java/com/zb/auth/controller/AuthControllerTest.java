package com.zb.auth.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.zb.common.ControllerTest;
import com.zb.dto.auth.AuthDto.SignIn.SignInRequest;
import com.zb.dto.auth.AuthDto.SignIn.SignInResponse;
import com.zb.dto.auth.AuthDto.SignUpCustomer.SignUpRequest;
import com.zb.dto.auth.AuthDto.SignUpManager;
import com.zb.type.UserRole;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTest extends ControllerTest {
    
    @Test
    @DisplayName("[성공]고객 회원가입")
    void signUpCustomer() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("customer2", "password", UserRole.ROLE_CUSTOMER,
          "010-1234-1234");

        // when
        ExtractableResponse<Response> response = saveCustomer(signUpRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("[성공]로그인-고객")
    void signIn_customer() {
        // given
        saveCustomer(customerSignUpRequest);

        // when
        ExtractableResponse<Response> response = signIn(customerSignInRequest);
        SignInResponse responseBody = response.body().as(SignInResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(responseBody.getToken()).isInstanceOf(String.class);
    }


    @Test
    @DisplayName("[성공]매니저 회원가입")
    void signUpManager() {
        // given
        SignUpManager.SignUpRequest signUpRequest = new SignUpManager.SignUpRequest("manager2", "password",
          UserRole.ROLE_MANAGER,
          "010-1234-1234");

        // when
        ExtractableResponse<Response> response = saveManager(signUpRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }


    @Test
    @DisplayName("[성공]로그인-매니저")
    void signIn_manager() {
        // given
        saveManager(managerSignUpRequest);

        // when
        ExtractableResponse<Response> response = signIn(managerSignInRequest);
        SignInResponse responseBody = response.body().as(SignInResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(responseBody.getToken()).isInstanceOf(String.class);
    }

    public static ExtractableResponse<Response> saveCustomer(SignUpRequest signUpRequest) {
        return given()
          .contentType(ContentType.JSON)
          .body(signUpRequest)
          .when()
          .post("/api/signup-customer")
          .then()
          .extract();
    }

    public static ExtractableResponse<Response> saveManager(SignUpManager.SignUpRequest signUpRequest) {
        return given()
          .contentType(ContentType.JSON)
          .body(signUpRequest)
          .when()
          .post("/api/signup-manager")
          .then()
          .extract();
    }

    public static ExtractableResponse<Response> signIn(SignInRequest signInRequest) {
        return given()
          .contentType(ContentType.JSON)
          .body(signInRequest)
          .when()
          .post("/api/signin")
          .then()
          .extract();
    }

    public static String getTokenForCustomer() {
        ExtractableResponse<Response> loginResponse = signIn(customerSignInRequest);
        return loginResponse.body().as(SignInResponse.class).getToken();
    }

    public static String getTokenForManager() {
        ExtractableResponse<Response> loginResponse = signIn(managerSignInRequest);
        return loginResponse.body().as(SignInResponse.class).getToken();
    }
}