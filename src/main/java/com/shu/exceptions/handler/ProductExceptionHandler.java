package com.shu.exceptions.handler;

import com.shu.exceptions.ProductException;
import com.shu.exceptions.StoreException;
import com.shu.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProductExceptionHandler {


    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleStoreException(ProductException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),
                "Product Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }
}
