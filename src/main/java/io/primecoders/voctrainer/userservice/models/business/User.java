package io.primecoders.voctrainer.userservice.models.business;

import io.primecoders.voctrainer.userservice.models.common.AccountStatus;
import io.primecoders.voctrainer.userservice.models.common.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Date created;
    private UserRole type;
    private AccountStatus accountStatus;
}
