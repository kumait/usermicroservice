package io.primecoders.voctrainer.userservice.infra.exceptions;

public class CodedException extends RuntimeException {
    protected String code;

    public CodedException(String code) {
        this.code = code;
    }

    public CodedException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CodedException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CodedException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    public CodedException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
