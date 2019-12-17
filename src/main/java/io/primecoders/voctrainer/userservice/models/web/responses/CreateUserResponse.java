package io.primecoders.voctrainer.userservice.models.web.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateUserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private Date creationDate;
}
