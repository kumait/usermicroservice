package io.primecoders.voctrainer.userservice.infra.exceptions.business;

import io.primecoders.voctrainer.userservice.infra.exceptions.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {
    public TokenExpiredException() {
        super("TOKEN_EXPIRED");
    }
}
