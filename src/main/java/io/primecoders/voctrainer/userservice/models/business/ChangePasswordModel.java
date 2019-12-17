package io.primecoders.voctrainer.userservice.models.business;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordModel {
    private String oldPassword;
    private String newPassword;
}
