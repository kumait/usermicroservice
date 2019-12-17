package io.primecoders.voctrainer.userservice.models.web.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateUserRequest {
    @NotEmpty
    @Size(min = 2, max = 100)
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 100)
    private String lastName;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String username;

    @NotEmpty
    @Size(min = 8, max = 100)
    private String password;
}
