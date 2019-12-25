package io.primecoders.voctrainer.userservice.models.web.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    private String token;
    private String refreshToken;
}
