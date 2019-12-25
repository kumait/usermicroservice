package io.primecoders.voctrainer.userservice.infra.exceptions.business;

import io.primecoders.voctrainer.userservice.infra.exceptions.UnauthorizedException;

public class AccountNotActiveException extends UnauthorizedException {
    public AccountNotActiveException() {
        super("ACCOUNT_NOT_ACTIVE");
    }
}
