package com.shu.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception for Category-related errors.
 *
 * <p>
 * Examples of usage:
 * <ul>
 *     <li>Category not found</li>
 *     <li>Duplicate category name</li>
 *     <li>Unauthorized category modification</li>
 * </ul>
 * </p>
 *
 * <p>
 * Each exception carries a {@link HttpStatus} which will be returned in the response.
 * Default status is {@link HttpStatus#BAD_REQUEST} (400).
 * </p>
 */
@Getter
public class CategoryException extends RuntimeException {

    private final HttpStatus status;

    /** Default status BAD_REQUEST (400) */
    public CategoryException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST; // Default to 400
    }

    /** Custom HTTP status */
    public CategoryException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
