package io.primecoders.voctrainer.userservice.infra.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends APIException {
    private static final int STATUS = HttpStatus.NOT_FOUND.value();

    public NotFoundException() {
        super(null, STATUS);
    }

    public NotFoundException(String code) {
        super(code, STATUS);
    }

    public NotFoundException(String code, String message) {
        super(code, STATUS, message);
    }

    public NotFoundException(String code, String message, Throwable cause) {
        super(code, STATUS, message, cause);
    }

    public NotFoundException(String code, Throwable cause) {
        super(code, STATUS, cause);
    }

    public NotFoundException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, STATUS, message, cause, enableSuppression, writableStackTrace);
    }
}
