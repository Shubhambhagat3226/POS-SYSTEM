package com.shu.exceptions.handler;

import com.shu.exceptions.CategoryException;
import com.shu.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Handles exceptions related to Category operations.
 *
 * <p>
 * Intercepts {@link CategoryException} thrown anywhere in the application
 * and returns a structured JSON response with relevant HTTP status and message.
 * </p>
 *
 * <p>
 * Using {@link Order} with {@link Ordered#HIGHEST_PRECEDENCE} ensures this handler
 * executes before global/fallback handlers.
 * </p>
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CategoryExceptionHandler {

    /**
     * Handles {@link CategoryException} thrown during category operations.
     *
     * @param ex      The exception instance containing the error message and HTTP status.
     * @param request The HTTP request object used to extract the endpoint path.
     * @return A structured {@link ErrorResponse} wrapped in {@link ResponseEntity}
     *         with the HTTP status defined in the exception.
     */
    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ErrorResponse> handleStoreException(
            CategoryException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),
                "Category Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }
}
