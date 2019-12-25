package io.primecoders.voctrainer.userservice.infra;

import io.primecoders.voctrainer.userservice.infra.exceptions.APIException;
import io.primecoders.voctrainer.userservice.models.web.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponse> handleExceptions(APIException ex, HttpServletResponse response) {
        return ResponseEntity.status(ex.getStatus()).body(ErrorResponse.from(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptions(Exception ex, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
