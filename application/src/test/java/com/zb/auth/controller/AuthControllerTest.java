package com.zb.auth.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.zb.dto.auth.AuthDto.SignIn.SignInRequest;
import com.zb.dto.auth.AuthDto.SignIn.SignInResponse;
import com.zb.dto.auth.AuthDto.SignUpCustomer.SignUpRequest;
import com.zb.dto.auth.AuthDto.SignUpManager;
import com.zb.repository.CustomerRepository;
import com.zb.repository.ManagerRepository;
import com.zb.type.UserRole;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthControllerTest {


    @LocalServerPort
    public int port;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        managerRepository.deleteAll();
    }

    @Nested
    class Customer {

        @Test
        @DisplayName("[성공]고객 회원가입")
        void signUpCustomer() {
            // given
            SignUpRequest signUpRequest = new SignUpRequest("customer", "password", UserRole.ROLE_CUSTOMER,
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
            SignUpRequest signUpRequest = new SignUpRequest("customer", "password", UserRole.ROLE_CUSTOMER,
              "010-1234-1234");
            saveCustomer(signUpRequest);

            // when
            SignInRequest signInRequest = new SignInRequest("customer", "password", UserRole.ROLE_CUSTOMER);
            ExtractableResponse<Response> response = signIn(signInRequest);
            SignInResponse responseBody = response.body().as(SignInResponse.class);

            // then
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(responseBody.getToken()).isInstanceOf(String.class);
        }
    }

    @Nested
    class Manager {

        @Test
        @DisplayName("[성공]매니저 회원가입")
        void signUpManager() {
            // given
            SignUpManager.SignUpRequest signUpRequest = new SignUpManager.SignUpRequest("manager", "password",
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
            SignUpManager.SignUpRequest signUpRequest = new SignUpManager.SignUpRequest("manager", "password",
              UserRole.ROLE_MANAGER,
              "010-1234-1234");
            saveManager(signUpRequest);

            // when
            SignInRequest signInRequest = new SignInRequest("manager", "password", UserRole.ROLE_MANAGER);
            ExtractableResponse<Response> response = signIn(signInRequest);
            SignInResponse responseBody = response.body().as(SignInResponse.class);

            // then
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(responseBody.getToken()).isInstanceOf(String.class);
        }
    }

    static ExtractableResponse<Response> saveCustomer(SignUpRequest signUpRequest) {
        return given()
          .contentType(ContentType.JSON)
          .body(signUpRequest)
          .when()
          .post("/api/signup-customer")
          .then()
          .extract();
    }

    static ExtractableResponse<Response> saveManager(SignUpManager.SignUpRequest signUpRequest) {
        return given()
          .contentType(ContentType.JSON)
          .body(signUpRequest)
          .when()
          .post("/api/signup-manager")
          .then()
          .extract();
    }

    static ExtractableResponse<Response> signIn(SignInRequest signInRequest) {
        return given()
          .contentType(ContentType.JSON)
          .body(signInRequest)
          .when()
          .post("/api/signin")
          .then()
          .extract();
    }
}