package io.primecoders.voctrainer.userservice.infra.security;

public enum TokenType {
    AUTHENTICATION("auth"), REFRESH("ref"), ACCOUNT_ACTIVATION("act"), PASSWORD_RESET("pwr");

    String code;

    TokenType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
