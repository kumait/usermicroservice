package io.primecoders.voctrainer.userservice.infra.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenUserInfo {
    private String username;
    private String roles;
}
