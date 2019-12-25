package io.primecoders.voctrainer.userservice.infra;

import io.primecoders.voctrainer.userservice.infra.exceptions.business.TokenExpiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public String handleExceptions(Exception ex, HttpServletResponse response) {
        try {
            response.sendError(460);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }
}
