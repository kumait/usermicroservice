package io.primecoders.voctrainer.userservice.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.primecoders.voctrainer.userservice.infra.ExtendedHttpStatus;
import io.primecoders.voctrainer.userservice.models.web.requests.LoginRequest;
import io.primecoders.voctrainer.userservice.models.web.responses.LoginResponse;
import io.primecoders.voctrainer.userservice.services.UserService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String ENV_SECURITY_LOGIN_URL = "security.login.url";
    private final UserService userService;
    private final TokenService tokenService;
    private final Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, TokenService tokenService, Environment environment) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl(Objects.requireNonNull(environment.getProperty(ENV_SECURITY_LOGIN_URL)));
        this.userService = userService;
        this.tokenService = tokenService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        } catch (IOException e) {
            throw new BadCredentialsException(null);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = authResult.getName();
        UserDetails userDetails = userService.loadUserByUsername(username);
        String roles = authResult.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((i1, i2) -> i1 + "," + i2)
                .orElse(null);
        String[] authenticationTokens = tokenService.createAuthenticationTokens(userDetails.getUsername(), roles);
        LoginResponse loginResponse = new LoginResponse(authenticationTokens[0], authenticationTokens[1]);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), loginResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        if (failed instanceof AccountExpiredException) {
            response.setStatus(ExtendedHttpStatus.ACCOUNT_NOT_ACTIVE.value());
        } else if (failed instanceof DisabledException) {
            response.setStatus(ExtendedHttpStatus.ACCOUNT_DISABLED.value());
        } else {
            super.unsuccessfulAuthentication(request, response, failed);
        }
    }
}
