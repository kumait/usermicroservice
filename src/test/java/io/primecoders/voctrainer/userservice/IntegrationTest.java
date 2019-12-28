package io.primecoders.voctrainer.userservice;

import io.primecoders.voctrainer.userservice.infra.ExtendedHttpStatus;
import io.primecoders.voctrainer.userservice.infra.security.TokenService;
import io.primecoders.voctrainer.userservice.models.common.AccountStatus;
import io.primecoders.voctrainer.userservice.models.entities.UserEntity;
import io.primecoders.voctrainer.userservice.models.web.requests.ChangePasswordRequest;
import io.primecoders.voctrainer.userservice.models.web.requests.CreateUserRequest;
import io.primecoders.voctrainer.userservice.models.web.requests.LoginRequest;
import io.primecoders.voctrainer.userservice.models.web.responses.*;
import io.primecoders.voctrainer.userservice.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private TokenService tokenService;

    private String getUrl(String uri) {
        return "http://localhost:" + port + uri;
    }

    private HttpHeaders headers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
        if (token != null) {
            headers.put("auth-token", Collections.singletonList(token));
        }
        return headers;
    }

    private CreateUserRequest getRandomUser() {
        return new CreateUserRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public ResponseEntity<CreateUserResponse> createUser(CreateUserRequest createUserRequest) {
        HttpEntity<CreateUserRequest> entity = new HttpEntity<>(createUserRequest, headers(null));
        return rest.exchange(getUrl("/users"), HttpMethod.POST, entity, CreateUserResponse.class);
    }

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers(null));
        return rest.exchange(getUrl("/login"), HttpMethod.POST, entity, LoginResponse.class);
    }

    public ResponseEntity<UserInfoResponse> getInfo(String token) {
        HttpEntity<Void> infoEntity = new HttpEntity<>(null, headers(token));
        return rest.exchange(getUrl("/users/info"), HttpMethod.GET, infoEntity, UserInfoResponse.class);
    }

    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        HttpEntity<RefreshTokenRequest> refreshTokenResponseHttpEntity = new HttpEntity<>(refreshTokenRequest, headers(null));
        return rest.exchange(getUrl("/users/refresh-token"), HttpMethod.POST, refreshTokenResponseHttpEntity, RefreshTokenResponse.class);
    }

    public ResponseEntity<Void> changePassword(String token, ChangePasswordRequest changePasswordRequest) {
        HttpEntity<ChangePasswordRequest> refreshTokenResponseHttpEntity = new HttpEntity<>(changePasswordRequest, headers(token));
        return rest.exchange(getUrl("/users/password"), HttpMethod.PUT, refreshTokenResponseHttpEntity, Void.class);
    }

    public CreateUserResponse createAndActivate(CreateUserRequest createUserRequest) {
        ResponseEntity<CreateUserResponse> createUserResponseResponseEntity = createUser(createUserRequest);
        UserEntity userEntity = userRepository.findByUsername(createUserRequest.getUsername());
        userEntity.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(userEntity);
        return createUserResponseResponseEntity.getBody();
    }

    @Test()
    @DisplayName("successful create user")
    public void testSuccessfulCreateUser() {
        CreateUserRequest createUserRequest = getRandomUser();
        ResponseEntity<CreateUserResponse> createUserResponseResponseEntity = createUser(createUserRequest);
        assertEquals(HttpStatus.CREATED, createUserResponseResponseEntity.getStatusCode());
        CreateUserResponse createUserResponse = createUserResponseResponseEntity.getBody();
        assertNotNull(createUserResponse);
        assertAll(() -> {
            assertEquals(createUserRequest.getUsername(), createUserResponse.getUsername());
            assertEquals(createUserRequest.getFirstName(), createUserResponse.getFirstName());
            assertEquals(createUserRequest.getLastName(), createUserResponse.getLastName());
            assertNotNull(createUserResponse.getId());
        });
    }

    @Test
    @DisplayName("create existing user")
    public void testFailingCreateUser() {
        CreateUserRequest createUserRequest = getRandomUser();
        createUser(createUserRequest);
        ResponseEntity<CreateUserResponse> createUserResponseResponseEntity = createUser(createUserRequest);
        assertEquals(HttpStatus.CONFLICT, createUserResponseResponseEntity.getStatusCode());
    }

    @Test()
    @DisplayName("wrong credentials login")
    public void testWrongCredentialsLogin() {
        LoginRequest loginRequest = new LoginRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        ResponseEntity<LoginResponse> loginResponseResponseEntity = login(loginRequest);
        assertEquals(loginResponseResponseEntity.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("account not active login")
    public void testAccountNotActiveLogin() {
        // create user
        CreateUserRequest createUserRequest = getRandomUser();
        createUser(createUserRequest);

        // login
        LoginRequest loginRequest = new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword());
        ResponseEntity<LoginResponse> loginResponseResponseEntity = login(loginRequest);
        assertEquals(ExtendedHttpStatus.ACCOUNT_NOT_ACTIVE.value(), loginResponseResponseEntity.getStatusCodeValue());
    }

    @Test
    @DisplayName("successful login")
    public void testSuccessfulLogin() {
        CreateUserRequest createUserRequest = getRandomUser();
        createAndActivate(createUserRequest);
        LoginRequest loginRequest = new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword());
        ResponseEntity<LoginResponse> loginResponseResponseEntity = login(loginRequest);
        assertAll(
                () -> assertEquals(HttpStatus.OK, loginResponseResponseEntity.getStatusCode()),
                () -> assertNotNull(loginResponseResponseEntity.getBody()),
                () -> assertNotNull(loginResponseResponseEntity.getBody().getToken()),
                () -> assertNotNull(loginResponseResponseEntity.getBody().getRefreshToken())
        );
    }

    @Test
    @DisplayName("get info")
    public void testGetInfo() {
        CreateUserRequest createUserRequest = getRandomUser();
        CreateUserResponse createUserResponse = createAndActivate(createUserRequest);
        LoginRequest loginRequest = new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword());
        LoginResponse loginResponse = login(loginRequest).getBody();

        ResponseEntity<UserInfoResponse> userInfoResponseResponseEntity = getInfo(loginResponse.getToken());
        assertEquals(HttpStatus.OK, userInfoResponseResponseEntity.getStatusCode());
        UserInfoResponse userInfoResponse = userInfoResponseResponseEntity.getBody();

        assertAll(
                () -> assertEquals(userInfoResponse.getId(), createUserResponse.getId()),
                () -> assertEquals(userInfoResponse.getUsername(), createUserResponse.getUsername()),
                () -> assertEquals(userInfoResponse.getFirstName(), createUserResponse.getFirstName()),
                () -> assertEquals(userInfoResponse.getLastName(), createUserResponse.getLastName())
        );
    }

    @Test()
    @DisplayName("token expiration")
    @Tag("slow")
    public void testTokenExpiration() {
        CreateUserRequest createUserRequest = getRandomUser();
        createAndActivate(createUserRequest);
        LoginRequest loginRequest = new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword());
        LoginResponse loginResponse = login(loginRequest).getBody();

        // sleep for 5 seconds so the token is expired
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResponseEntity<UserInfoResponse> userInfoResponseResponseEntity = getInfo(loginResponse.getToken());
        assertEquals(ExtendedHttpStatus.TOKEN_EXPIRED.value(), userInfoResponseResponseEntity.getStatusCodeValue());
    }

    @Test
    @DisplayName("successful refresh token")
    public void testRefreshToken() {
        CreateUserRequest createUserRequest = getRandomUser();
        createAndActivate(createUserRequest);
        LoginResponse loginResponse = login(new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword())).getBody();
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(loginResponse.getRefreshToken());
        ResponseEntity<RefreshTokenResponse> refreshTokenResponseResponseEntity = refreshToken(refreshTokenRequest);
        assertEquals(HttpStatus.OK, refreshTokenResponseResponseEntity.getStatusCode());
        RefreshTokenResponse refreshTokenResponse = refreshTokenResponseResponseEntity.getBody();
        assertAll(
                () -> assertNotNull(refreshTokenResponse),
                () -> assertNotNull(refreshTokenResponse.getToken()),
                () -> assertNotNull(refreshTokenResponse.getRefreshToken())
        );
    }

    @Test
    @DisplayName("expired refresh token")
    @Tag("slow")
    public void testRefreshTokenExpiration() {
        CreateUserRequest createUserRequest = getRandomUser();
        createAndActivate(createUserRequest);
        LoginResponse loginResponse = login(new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword())).getBody();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(loginResponse.getRefreshToken());
        ResponseEntity<RefreshTokenResponse> refreshTokenResponseResponseEntity = refreshToken(refreshTokenRequest);
        assertEquals(ExtendedHttpStatus.TOKEN_EXPIRED.value(), refreshTokenResponseResponseEntity.getStatusCodeValue());
    }

    @Test
    @DisplayName("failure change password (wrong old password)")
    public void testFailureChangePassword1() {
        CreateUserRequest createUserRequest = getRandomUser();
        createAndActivate(createUserRequest);
        LoginRequest loginRequest = new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword());
        LoginResponse loginResponse = login(loginRequest).getBody();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        ResponseEntity<Void> changePasswordResponseEntity = changePassword(loginResponse.getToken(), changePasswordRequest);
        assertEquals(HttpStatus.FORBIDDEN, changePasswordResponseEntity.getStatusCode());
    }

    @Test
    @DisplayName("failure change password (wrong token)")
    public void testFailureChangePassword2() {
        CreateUserRequest createUserRequest = getRandomUser();
        createAndActivate(createUserRequest);
        LoginRequest loginRequest = new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword());
        LoginResponse loginResponse = login(loginRequest).getBody();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(createUserRequest.getPassword(), UUID.randomUUID().toString());
        String fakeToken = loginResponse.getToken() + "abc";
        ResponseEntity<Void> changePasswordResponseEntity = changePassword(fakeToken, changePasswordRequest);
        assertEquals(ExtendedHttpStatus.INVALID_TOKEN.value(), changePasswordResponseEntity.getStatusCodeValue());
    }

    @Test
    @DisplayName("success change password")
    public void testChangePassword() {
        CreateUserRequest createUserRequest = getRandomUser();
        createUserRequest.setPassword("12345678");
        createAndActivate(createUserRequest);
        LoginResponse loginResponse = login(new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword())).getBody();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(createUserRequest.getPassword(), UUID.randomUUID().toString());
        ResponseEntity<Void> changePasswordResponseEntity = changePassword(loginResponse.getToken(), changePasswordRequest);
        assertEquals(HttpStatus.OK, changePasswordResponseEntity.getStatusCode());

        LoginRequest oldPasswordLoginRequest = new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword());
        LoginRequest newPasswordLoginRequest = new LoginRequest(createUserRequest.getUsername(), changePasswordRequest.getNewPassword());

        assertEquals(HttpStatus.FORBIDDEN, login(oldPasswordLoginRequest).getStatusCode());
        assertEquals(HttpStatus.OK, login(newPasswordLoginRequest).getStatusCode());
    }
}
