package com.shu.exceptions.handler;

import com.shu.exceptions.UserException;
import com.shu.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Handles user-related custom exceptions
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {

    /**
     * Handles custom user-related exceptions thrown by the application.
     * Example cases: invalid login, duplicate email, unauthorized role creation.
     * <p>
     * HTTP Status: 400 (Bad Request) by default.
     *
     * @param ex      the UserException instance containing details
     * @param request the HttpServletRequest to extract the request path
     * @return a structured ErrorResponse wrapped in ResponseEntity
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),
                "User Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
