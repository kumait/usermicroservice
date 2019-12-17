package io.primecoders.voctrainer.userservice.models.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordModel {
    private String token;
    private String newPassword;
}
