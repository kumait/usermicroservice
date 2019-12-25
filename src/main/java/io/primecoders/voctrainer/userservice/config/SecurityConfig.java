package io.primecoders.voctrainer.userservice.config;

import io.primecoders.voctrainer.userservice.infra.security.AuthenticationFilter;
import io.primecoders.voctrainer.userservice.infra.security.AuthorizationFilter;
import io.primecoders.voctrainer.userservice.infra.security.TokenService;
import io.primecoders.voctrainer.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String ENV_SECURITY_LOGIN_URL = "security.login.url";
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @Autowired
    public SecurityConfig(UserService userService, TokenService tokenService, PasswordEncoder passwordEncoder, Environment environment) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
        this.tokenService = tokenService;
    }

    private AbstractAuthenticationProcessingFilter authenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, tokenService, environment);
        authenticationFilter.setFilterProcessesUrl(Objects.requireNonNull(environment.getProperty(ENV_SECURITY_LOGIN_URL)));
        return authenticationFilter;
    }

    private AbstractAuthenticationProcessingFilter refreshTokenFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, tokenService, environment);
        authenticationFilter.setFilterProcessesUrl(Objects.requireNonNull(environment.getProperty(ENV_SECURITY_LOGIN_URL)));
        return authenticationFilter;
    }

    private BasicAuthenticationFilter authorizationFilter() throws Exception {
        return new AuthorizationFilter(authenticationManager(), tokenService, environment);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // allow user to register
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll();

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/refresh-token").permitAll();

        // allow status access
        http.authorizeRequests().antMatchers("/status/**").permitAll();

        http.authorizeRequests().antMatchers("/users/create-test-users").permitAll();

        // all other places require authentication
        http.authorizeRequests().anyRequest().authenticated();

        // add the login filter
        http.addFilter(authenticationFilter());

        // add authorization filter
        http.addFilter(authorizationFilter());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
