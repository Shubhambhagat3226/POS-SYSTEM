package com.shu.exceptions;

/**
 * Custom exception for user-related errors
 * (like invalid login, duplicate email, etc.)
 */
public class UserException extends RuntimeException{
    public UserException(String message) {
        super(message);
    }
}
