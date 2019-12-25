package io.primecoders.voctrainer.userservice.models.web.responses;

import io.primecoders.voctrainer.userservice.infra.exceptions.CodedException;

public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse(String code) {
        this.code = code;
    }

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(Exception ex) {
        this.message = ex.getMessage();
        if (ex instanceof CodedException) {
            this.code = ((CodedException)ex).getCode();
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
