package io.primecoders.voctrainer.userservice.models.entities;

import io.primecoders.voctrainer.userservice.models.common.AccountStatus;
import io.primecoders.voctrainer.userservice.models.common.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    @Column(name = "account_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountStatus accountStatus;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
}
