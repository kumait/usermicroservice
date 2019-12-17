package io.primecoders.voctrainer.userservice.controllers;

import io.primecoders.voctrainer.userservice.models.business.User;
import io.primecoders.voctrainer.userservice.models.web.requests.CreateUserRequest;
import io.primecoders.voctrainer.userservice.models.web.responses.CreateUserResponse;
import io.primecoders.voctrainer.userservice.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
