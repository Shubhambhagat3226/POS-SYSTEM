package com.shu.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception for user-related errors
 * (like invalid login, duplicate email, etc.)
 *
 * Now includes an HttpStatus to allow for dynamic and semantically correct
 * error responses from the GlobalExceptionHandler.
 */
@Getter
public class UserException extends RuntimeException{

    private final HttpStatus status;

    /**
     * Constructor with a message and a default status of BAD_REQUEST.
     * @param message The error message.
     */
    public UserException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST; // Default to 400
    }

    /**
     * Constructor with a specific message and HttpStatus.
     * @param message The error message.
     * @param status The HTTP status to be returned to the client.
     */
    public UserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
