package io.primecoders.voctrainer.userservice.infra.exceptions.business;

import io.primecoders.voctrainer.userservice.infra.exceptions.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {
    public InvalidTokenException() {
        super("INVALID_TOKEN");
    }
}
