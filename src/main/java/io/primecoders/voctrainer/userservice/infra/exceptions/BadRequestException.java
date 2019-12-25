package io.primecoders.voctrainer.userservice.infra.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends APIException {
    private static final int STATUS = HttpStatus.BAD_REQUEST.value();

    public BadRequestException() {
        super(null, STATUS);
    }

    public BadRequestException(String code) {
        super(code, STATUS);
    }

    public BadRequestException(String code, String message) {
        super(code, STATUS, message);
    }

    public BadRequestException(String code, String message, Throwable cause) {
        super(code, STATUS, message, cause);
    }

    public BadRequestException(String code, Throwable cause) {
        super(code, STATUS, cause);
    }

    public BadRequestException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, STATUS, message, cause, enableSuppression, writableStackTrace);
    }
}
