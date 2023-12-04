package com.zb.common;

import static com.zb.auth.controller.AuthControllerTest.getTokenForCustomer;
import static com.zb.auth.controller.AuthControllerTest.getTokenForManager;
import static com.zb.auth.controller.AuthControllerTest.saveCustomer;
import static com.zb.auth.controller.AuthControllerTest.saveManager;

import com.zb.dto.auth.AuthDto.SignIn.SignInRequest;
import com.zb.dto.auth.AuthDto.SignUpCustomer;
import com.zb.dto.auth.AuthDto.SignUpCustomer.SignUpRequest;
import com.zb.dto.auth.AuthDto.SignUpManager;
import com.zb.type.UserRole;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ControllerTest {

    protected static String customerToken;
    protected static String managerToken;
    protected static final SignUpCustomer.SignUpRequest customerSignUpRequest = new SignUpRequest(
      "customer", "password", UserRole.ROLE_CUSTOMER, "010-1234-1234");
    protected static final SignUpManager.SignUpRequest managerSignUpRequest = new SignUpManager.SignUpRequest(
      "manager", "password", UserRole.ROLE_MANAGER, "010-1234-1234");
    protected static final SignInRequest customerSignInRequest = new SignInRequest("customer", "password",
      UserRole.ROLE_CUSTOMER);
    protected static final SignInRequest managerSignInRequest = new SignInRequest("manager", "password",
      UserRole.ROLE_MANAGER);

    @LocalServerPort
    public int port;

    @Autowired
    public DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        saveCustomer(customerSignUpRequest);
        saveManager(managerSignUpRequest);
        customerToken = getTokenForCustomer();
        managerToken = getTokenForManager();
    }

    @AfterEach
    void deleteAll() {
        databaseCleaner.execute();
    }
}
