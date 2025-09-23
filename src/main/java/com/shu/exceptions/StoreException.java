package com.shu.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception for store-related errors in the POS system
 * Examples: invalid store data, blocked store, failed operations
 */
public class StoreException extends RuntimeException {
    private final HttpStatus status;


    /** Default status BAD_REQUEST (400) */
    public StoreException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST; // Default to 400
    }

    /** Custom HTTP status */
    public StoreException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
