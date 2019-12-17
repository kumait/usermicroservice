package io.primecoders.voctrainer.userservice.models.web.requests;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ResetPasswordRequest {
    @NotEmpty
    private String token;

    @Length(min = 6, max = 100)
    private String newPassword;
}
