package com.shu.exceptions;

import com.shu.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * GlobalExceptionHandler
 *
 * <p>
 * This class provides a centralized exception handling mechanism for the entire Spring Boot application.
 * Using {@link ControllerAdvice}, it intercepts exceptions thrown by controllers or services and returns
 * a consistent, structured JSON response to the client.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *   <li>Standardize error responses across all REST endpoints.</li>
 *   <li>Convert custom application exceptions (like {@link UserException}) into HTTP responses with
 *       appropriate status codes.</li>
 *   <li>Handle Spring Security exceptions (like {@link org.springframework.security.core.userdetails.UsernameNotFoundException})
 *       gracefully.</li>
 *   <li>Provide a fallback handler for any uncaught exceptions to prevent internal server errors without context.</li>
 * </ul>
 *
 * <p><b>Design:</b></p>
 * <ul>
 *   <li>Returns an {@link com.shu.payload.response.ErrorResponse} object containing:
 *     <ul>
 *       <li><b>timestamp:</b> when the error occurred</li>
 *       <li><b>status:</b> HTTP status code (e.g., 400, 404, 500)</li>
 *       <li><b>error:</b> short description of the type of error</li>
 *       <li><b>message:</b> detailed message for debugging or user feedback</li>
 *       <li><b>path:</b> the URL endpoint where the error occurred</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p><b>Benefits:</b></p>
 * <ul>
 *   <li>Provides a <b>professional-grade error reporting system</b> for APIs.</li>
 *   <li>Separates <b>business logic exceptions</b> from <b>security/authentication exceptions</b>.</li>
 *   <li>Easily extensible for future exception types.</li>
 *   <li>Ensures clients always receive structured error data instead of raw stack traces.</li>
 * </ul>
 *
 * <p><b>Example JSON Response for a Failed Login:</b></p>
 * <pre>
 * {
 *   "timestamp": "2025-09-22T21:33:03.206",
 *   "status": 404,
 *   "error": "User Not Found",
 *   "message": "User not found",
 *   "path": "/auth/login"
 * }
 * </pre>
 *
 * <p>
 * This format is consistent for all handled exceptions, improving maintainability, debugging,
 * and client-side integration.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

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
                HttpStatus.BAD_REQUEST.value(),   // or 403 if preferred
                "User Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles Spring Security's UsernameNotFoundException.
     * This occurs when authentication fails due to a non-existent user.
     * <p>
     * HTTP Status: 404 (Not Found)
     *
     * @param ex      the UsernameNotFoundException instance
     * @param request the HttpServletRequest to extract the request path
     * @return a structured ErrorResponse wrapped in ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles any uncaught or unexpected exceptions in the application.
     * Acts as a fallback to ensure that no exception goes unhandled.
     * <p>
     * HTTP Status: 500 (Internal Server Error)
     *
     * @param ex      the Exception instance
     * @param request the HttpServletRequest to extract the request path
     * @return a structured ErrorResponse wrapped in ResponseEntity
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "User Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
