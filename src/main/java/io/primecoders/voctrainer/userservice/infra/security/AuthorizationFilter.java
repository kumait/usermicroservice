package io.primecoders.voctrainer.userservice.infra.security;

import io.primecoders.voctrainer.userservice.infra.exceptions.APIException;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    public static final String ENV_AUTH_TOKEN_HEADER = "security.authentication-token-header";
    private final TokenService tokenService;
    private final Environment environment;

    public AuthorizationFilter(AuthenticationManager authenticationManager, TokenService tokenService, Environment environment) {
        super(authenticationManager);
        this.tokenService = tokenService;
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            if (authentication == null) {
                chain.doFilter(request, response);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (APIException ex) {
            response.setStatus(ex.getStatus());
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(environment.getProperty(ENV_AUTH_TOKEN_HEADER));
        if (token == null || token.length() == 0) {
            return null;
        }

        TokenUserInfo tokenUserInfo = tokenService.verifyAndGet(token, TokenType.AUTHENTICATION);
        String[] roles = tokenUserInfo.getRoles().split(",");
        List<GrantedAuthority> authorities = Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(tokenUserInfo.getUsername(), null, authorities);
    }
}
