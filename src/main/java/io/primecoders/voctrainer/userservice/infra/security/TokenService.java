package io.primecoders.voctrainer.userservice.infra.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.primecoders.voctrainer.userservice.infra.exceptions.authorization.InvalidTokenException;
import io.primecoders.voctrainer.userservice.infra.exceptions.authorization.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Service
public class TokenService {
    private static final String ISSUER = "voctrainer";
    private static final String TYPE = "typ";
    private static final String ROLE = "rol";

    @Value("${security.authentication.secret-key}")
    private String secretKeyString;

    @Value("${security.token.authentication.expiration}")
    private int authenticationTokenExpiration;

    @Value("${security.token.refresh.expiration}")
    private int refreshTokenExpiration;

    @Value("${security.token.activation.expiration}")
    private int activationTokenExpiration;

    @Value("${security.token.password-reset.expiration}")
    private int passwordResetTokenExpiration;

    private SecretKey secretKey;

    @PostConstruct
    private void init() {
        this.secretKey = new SecretKeySpec(Decoders.BASE64.decode(secretKeyString), SignatureAlgorithm.HS512.getJcaName());
    }

    public String[] createAuthenticationTokens(String username, String roles) {
        long now = System.currentTimeMillis();
        String authenticationToken = Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(now))
                .setSubject(username)
                .claim(ROLE, roles)
                .claim(TYPE, TokenType.AUTHENTICATION.getCode())
                .setExpiration(new Date(now + authenticationTokenExpiration * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(now))
                .setSubject(username)
                .claim(ROLE, roles)
                .claim(TYPE, TokenType.REFRESH.getCode())
                .setExpiration(new Date(now + refreshTokenExpiration * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        return new String[] {authenticationToken, refreshToken};
    }

    public String createAccountActivationToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(now))
                .setSubject(username)
                .claim(TYPE, TokenType.ACCOUNT_ACTIVATION.getCode())
                .setExpiration(new Date(now + activationTokenExpiration * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createPasswordResetToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(now))
                .setSubject(username)
                .claim(TYPE, TokenType.PASSWORD_RESET.getCode())
                .setExpiration(new Date(now + passwordResetTokenExpiration * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Jws<Claims> verifyAndParse(String token, TokenType type) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException();
        } catch (Exception ex) {
            throw new InvalidTokenException();
        }

        if (!claimsJws.getBody().get(TYPE).equals(type.getCode())) {
            throw new InvalidTokenException();
        }
        return claimsJws;
    }

    public TokenUserInfo verifyAndGet(String token, TokenType type) {
        Jws<Claims> claimsJws = verifyAndParse(token, type);
        return new TokenUserInfo(claimsJws.getBody().getSubject(), claimsJws.getBody().get(ROLE).toString());
    }
}
