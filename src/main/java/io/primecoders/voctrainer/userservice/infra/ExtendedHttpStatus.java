package io.primecoders.voctrainer.userservice.infra;

public enum ExtendedHttpStatus {
    ACCOUNT_NOT_ACTIVE(455),
    ACCOUNT_DISABLED(456),
    INVALID_TOKEN(460),
    TOKEN_EXPIRED(461);

    private final int value;

    ExtendedHttpStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
