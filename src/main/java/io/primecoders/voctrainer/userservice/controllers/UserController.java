package io.primecoders.voctrainer.userservice.controllers;

import io.primecoders.voctrainer.userservice.models.business.RefreshTokenModel;
import io.primecoders.voctrainer.userservice.models.business.User;
import io.primecoders.voctrainer.userservice.models.web.requests.CreateUserRequest;
import io.primecoders.voctrainer.userservice.models.web.responses.CreateUserResponse;
import io.primecoders.voctrainer.userservice.models.web.responses.RefreshTokenRequest;
import io.primecoders.voctrainer.userservice.models.web.responses.RefreshTokenResponse;
import io.primecoders.voctrainer.userservice.models.web.responses.UserInfoResponse;
import io.primecoders.voctrainer.userservice.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        User user = mapper.map(createUserRequest, User.class);
        user = userService.createUser(user);
        CreateUserResponse response = mapper.map(user, CreateUserResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getInfo() {
        User user = userService.getInfo();
        UserInfoResponse userInfoResponse = mapper.map(user, UserInfoResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(userInfoResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenModel refreshTokenModel = userService.refreshToken(refreshTokenRequest.getRefreshToken());
        RefreshTokenResponse refreshTokenResponse = mapper.map(refreshTokenModel, RefreshTokenResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(refreshTokenResponse);
    }

    @PostMapping("/create-test-users")
    public ResponseEntity<String> createTestUsers(@RequestParam int count) {
        for (int i = 0; i < count; i++) {
            CreateUserRequest createUserRequest = new CreateUserRequest(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString()
            );
            User user = mapper.map(createUserRequest, User.class);
            user = userService.createUser(user);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Created users: " + count);

    }
}
