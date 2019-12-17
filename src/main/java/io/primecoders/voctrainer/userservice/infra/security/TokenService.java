package io.primecoders.voctrainer.userservice.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.primecoders.voctrainer.userservice.infra.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Objects;

@Service
public class TokenService {
    private static final String ENV_AUTH_SECRET_KEY = "security.authentication.secret-key";
    private static final String ENV_TOKEN_AUTH_EXPIRATION = "security.token.authentication.expiration";
    private static final String ENV_TOKEN_ACTIVATION_EXPIRATION = "security.token.activation.expiration";
    private static final String ENV_TOKEN_PASSWORD_RESET_EXPIRATION = "security.token.password-reset.expiration";
    private static final String ISSUER = "voctrainer";
    private static final String AUTH = "auth";
    private static final String RENEW = "rn";
    private static final String USER = "usr";
    private static final String ROLE = "rol";
    private static final String ACTIVATE = "act";
    private static final String PASSWORD_RESET = "pwr";

    private final SecretKey secretKey;
    private final Environment environment;

    @Autowired
    public TokenService(Environment environment) {
        this.environment = environment;
        String secretKeyString = Objects.requireNonNull(environment.getProperty(ENV_AUTH_SECRET_KEY));
        this.secretKey = new SecretKeySpec(Decoders.BASE64.decode(secretKeyString), SignatureAlgorithm.HS512.getJcaName());
    }

    public String createToken(String subject, String username, Date expiration) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(now))
                .setSubject(subject)
                .claim(USER, username)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createAuthenticationToken(String username) {
        long now = System.currentTimeMillis();
        int expiration = Integer.parseInt(Objects.requireNonNull(environment.getProperty(ENV_TOKEN_AUTH_EXPIRATION))) * 1000;
        return createToken(AUTH, username, new Date(now + expiration));
    }

    public String createAccountActivationToken(String username) {
        long now = System.currentTimeMillis();
        int expiration = Integer.parseInt(Objects.requireNonNull(environment.getProperty(ENV_TOKEN_ACTIVATION_EXPIRATION))) * 1000;
        return createToken(ACTIVATE, username, new Date(now + expiration));
    }

    public String createPasswordResetToken(String username) {
        long now = System.currentTimeMillis();
        int expiration = Integer.parseInt(Objects.requireNonNull(environment.getProperty(ENV_TOKEN_PASSWORD_RESET_EXPIRATION))) * 1000;
        return createToken(PASSWORD_RESET, username, new Date(now + expiration));
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public boolean validateDates(Jws<Claims> claimsJws) {
        return claimsJws.getBody().getExpiration().getTime() > System.currentTimeMillis() &&
                claimsJws.getBody().getNotBefore().getTime() < System.currentTimeMillis();
    }

    public String getUsernameFromAccountActivationToken(String token) {
        Jws<Claims> claimsJws = parse(token);
        if (!validateDates(claimsJws) || !claimsJws.getBody().getSubject().equals(ACTIVATE)) {
            throw new UnauthorizedException();
        }
        return claimsJws.getBody().get(USER, String.class);
    }

    public String getUsernameFromPasswordResetToken(String token) {
        Jws<Claims> claimsJws = parse(token);
        if (!validateDates(claimsJws) || !claimsJws.getBody().getSubject().equals(PASSWORD_RESET)) {
            throw new UnauthorizedException();
        }
        return claimsJws.getBody().get(USER, String.class);
    }
}
