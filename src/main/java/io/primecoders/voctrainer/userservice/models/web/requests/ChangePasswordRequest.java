package io.primecoders.voctrainer.userservice.models.web.requests;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ChangePasswordRequest {
    @Length(min = 6, max = 100)
    private String oldPassword;

    @Length(min = 6, max = 100)
    private String newPassword;
}
