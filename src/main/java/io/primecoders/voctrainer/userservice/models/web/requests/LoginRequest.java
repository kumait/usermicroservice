package io.primecoders.voctrainer.userservice.models.web.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotEmpty
    @Length(min = 3, max = 100)
    private String username;

    @NotEmpty
    @Length(min = 6, max = 100)
    private String password;
}
