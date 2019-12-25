package io.primecoders.voctrainer.userservice.infra.exceptions.authorization;

import io.primecoders.voctrainer.userservice.infra.exceptions.APIException;

public class InvalidTokenException extends APIException {
    private static final int STATUS = 460;

    public InvalidTokenException() {
        super(null, STATUS);
    }

    public InvalidTokenException(String code) {
        super(code, STATUS);
    }

    public InvalidTokenException(String code, String message) {
        super(code, STATUS, message);
    }

    public InvalidTokenException(String code, String message, Throwable cause) {
        super(code, STATUS, message, cause);
    }

    public InvalidTokenException(String code, Throwable cause) {
        super(code, STATUS, cause);
    }

    public InvalidTokenException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, STATUS, message, cause, enableSuppression, writableStackTrace);
    }
}
