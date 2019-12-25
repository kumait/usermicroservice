package io.primecoders.voctrainer.userservice.models.web.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
}
