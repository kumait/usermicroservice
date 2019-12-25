package io.primecoders.voctrainer.userservice.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class BadRequestException extends CodedException {

    public BadRequestException() {
        super("BAD_REQUEST");
    }

    public BadRequestException(String code) {
        super(code);
    }

    public BadRequestException(String code, String message) {
        super(code, message);
    }

    public BadRequestException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BadRequestException(Throwable cause, String code) {
        super(cause, code);
    }

    public BadRequestException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
