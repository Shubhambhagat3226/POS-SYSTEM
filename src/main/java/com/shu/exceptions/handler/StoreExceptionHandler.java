package com.shu.exceptions.handler;

import com.shu.exceptions.StoreException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import com.shu.payload.response.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Handles store-related custom exceptions
 *
 * <p><b>Example JSON Response:</b></p>
 * <pre>
 * {
 *   "timestamp": "2025-09-23T21:40:00.000",
 *   "status": 400,
 *   "error": "Store Error",
 *   "message": "Store cannot be created with a duplicate brand name",
 *   "path": "/stores"
 * }
 * </pre>
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StoreExceptionHandler {

    /**
     * Handles custom store-related exceptions thrown by the application.
     *
     * @param ex      the StoreException instance containing details
     * @param request the HttpServletRequest to extract the request path
     * @return a structured ErrorResponse wrapped in ResponseEntity
     */
    @ExceptionHandler(StoreException.class)
    public ResponseEntity<ErrorResponse> handleStoreException(StoreException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),
                "Store Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }
}
