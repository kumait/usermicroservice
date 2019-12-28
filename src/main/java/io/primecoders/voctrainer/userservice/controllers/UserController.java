package io.primecoders.voctrainer.userservice.controllers;

import io.primecoders.voctrainer.userservice.models.business.ChangePasswordModel;
import io.primecoders.voctrainer.userservice.models.business.RefreshTokenModel;
import io.primecoders.voctrainer.userservice.models.business.User;
import io.primecoders.voctrainer.userservice.models.web.requests.ChangePasswordRequest;
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

    @PutMapping("/activate")
    public void activateAccount(@RequestParam String activationToken) {
        userService.activateAccount(activationToken);
    }

    @PutMapping("/password")
    public void changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        ChangePasswordModel changePasswordModel = mapper.map(changePasswordRequest, ChangePasswordModel.class);
        userService.changePassword(changePasswordModel);
    }
}
