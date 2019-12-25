package io.primecoders.voctrainer.userservice.infra.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends APIException {
    private static final int STATUS = HttpStatus.FORBIDDEN.value();

    public ForbiddenException() {
        super(null, STATUS);
    }

    public ForbiddenException(String code) {
        super(code, STATUS);
    }

    public ForbiddenException(String code, String message) {
        super(code, STATUS, message);
    }

    public ForbiddenException(String code, String message, Throwable cause) {
        super(code, STATUS, message, cause);
    }

    public ForbiddenException(String code, Throwable cause) {
        super(code, STATUS, cause);
    }

    public ForbiddenException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, STATUS, message, cause, enableSuppression, writableStackTrace);
    }
}
