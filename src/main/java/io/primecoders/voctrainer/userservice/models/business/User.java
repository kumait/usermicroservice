package io.primecoders.voctrainer.userservice.models.business;

import io.primecoders.voctrainer.userservice.models.common.AccountStatus;
import io.primecoders.voctrainer.userservice.models.common.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private UserType type;
    private AccountStatus accountStatus;
}
