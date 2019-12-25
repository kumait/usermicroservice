package io.primecoders.voctrainer.userservice.models.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenModel {
    private String token;
    private String refreshToken;
}
