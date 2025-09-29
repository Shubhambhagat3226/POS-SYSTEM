package com.shu.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ErrorResponse
 *
 * Standardized structure for API error responses.
 * Used by GlobalExceptionHandler (or any exception handling mechanism)
 * to ensure consistent, readable, and maintainable error reporting.
 *
 * Fields:
 * - timestamp: when the error occurred (helps with debugging and logs)
 * - status: HTTP status code (e.g., 400, 404, 500)
 * - error: short description of error type (e.g., "User Not Found", "Validation Error")
 * - message: detailed message or exception message
 * - path: the endpoint URL that triggered the error
 *
 * Usage:
 * - Returned in the body of ResponseEntity when exceptions occur.
 * - Helps frontend or API consumers to handle errors uniformly.
 *
 * Example:
 * {
 *   "timestamp": "2025-09-22T21:33:03.206",
 *   "status": 404,
 *   "error": "User Not Found",
 *   "message": "User not found",
 *   "path": "/auth/login"
 * }
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
