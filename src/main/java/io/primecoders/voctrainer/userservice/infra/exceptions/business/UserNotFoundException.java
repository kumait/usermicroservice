package io.primecoders.voctrainer.userservice.infra.exceptions.business;

import io.primecoders.voctrainer.userservice.infra.exceptions.UnauthorizedException;

public class UserNotFoundException extends UnauthorizedException {
    public UserNotFoundException() {
        super("INVALID_TOKEN");
    }
}
