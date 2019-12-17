package io.primecoders.voctrainer.userservice.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.primecoders.voctrainer.userservice.models.web.requests.LoginRequest;
import io.primecoders.voctrainer.userservice.services.UserService;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTH_TOKEN_HEADER = "security.authentication.header";
    private final UserService userService;
    private final TokenService tokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, TokenService tokenService, Environment environment) {
        setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = authResult.getName();
        UserDetails userDetails = userService.loadUserByUsername(username);
        String authenticationToken = tokenService.createAuthenticationToken(userDetails.getUsername());
        response.addHeader(AUTH_TOKEN_HEADER, authenticationToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
