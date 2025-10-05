package com.shu.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductException extends RuntimeException {

    private final HttpStatus status;


    /** Default status BAD_REQUEST (400) */
    public ProductException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST; // Default to 400
    }

    /** Custom HTTP status */
    public ProductException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
