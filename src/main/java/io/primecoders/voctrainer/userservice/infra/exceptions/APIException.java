package io.primecoders.voctrainer.userservice.infra.exceptions;

public class APIException extends RuntimeException {
    private String code;
    private int status;

    public APIException(String code, int status) {
        this.code = code;
        this.status = status;
    }

    public APIException(String code, int status, String message) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public APIException(String code, int status, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }

    public APIException(String code, int status, Throwable cause) {
        super(cause);
        this.code = code;
        this.status = status;
    }

    public APIException(String code, int status, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
