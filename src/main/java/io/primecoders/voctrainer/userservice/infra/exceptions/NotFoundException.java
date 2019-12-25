package io.primecoders.voctrainer.userservice.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not found")
public class NotFoundException extends CodedException {

    public NotFoundException() {
        super("NOT_FOUND");
    }

    public NotFoundException(String code) {
        super(code);
    }

    public NotFoundException(String code, String message) {
        super(code, message);
    }

    public NotFoundException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public NotFoundException(Throwable cause, String code) {
        super(cause, code);
    }

    public NotFoundException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
