package io.primecoders.voctrainer.userservice.models.web.responses;

import io.primecoders.voctrainer.userservice.infra.exceptions.APIException;

public class ErrorResponse {
    private String code;
    private String message;

    public static ErrorResponse from(APIException ex) {
        return new ErrorResponse(ex.getCode(), ex.getMessage());
    }

    public ErrorResponse() {
    }

    public ErrorResponse(String code) {
        this.code = code;
    }

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
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
