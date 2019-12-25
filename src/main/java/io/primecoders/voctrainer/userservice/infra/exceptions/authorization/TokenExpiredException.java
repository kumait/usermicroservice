package io.primecoders.voctrainer.userservice.infra.exceptions.authorization;

import io.primecoders.voctrainer.userservice.infra.ExtendedHttpStatus;
import io.primecoders.voctrainer.userservice.infra.exceptions.APIException;

public class TokenExpiredException extends APIException {
    private static final int STATUS = ExtendedHttpStatus.TOKEN_EXPIRED.value();

    public TokenExpiredException() {
        super(null, STATUS);
    }

    public TokenExpiredException(String code) {
        super(code, STATUS);
    }

    public TokenExpiredException(String code, String message) {
        super(code, STATUS, message);
    }

    public TokenExpiredException(String code, String message, Throwable cause) {
        super(code, STATUS, message, cause);
    }

    public TokenExpiredException(String code, Throwable cause) {
        super(code, STATUS, cause);
    }

    public TokenExpiredException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, STATUS, message, cause, enableSuppression, writableStackTrace);
    }
}
