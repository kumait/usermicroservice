package io.primecoders.voctrainer.userservice.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
public class UnauthorizedException extends CodedException {

    public UnauthorizedException() {
        super(null);
    }

    public UnauthorizedException(String code) {
        super(code);
    }

    public UnauthorizedException(String code, String message) {
        super(code, message);
    }

    public UnauthorizedException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UnauthorizedException(Throwable cause, String code) {
        super(cause, code);
    }

    public UnauthorizedException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
