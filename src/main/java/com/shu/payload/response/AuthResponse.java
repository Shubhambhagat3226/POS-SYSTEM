package com.shu.payload.response;

import com.shu.model.dto.UserDto;
import lombok.Data;

/**
 * AuthResponse DTO
 *
 * Represents the response for authentication-related APIs.
 * Includes JWT token and user details (excluding password).
 *
 * Returned after successful login or registration.
 * Contains:
 * - JWT token (used for future authenticated requests).
 * - Message (status/info).
 * - UserDto (basic user details).
 */
@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private UserDto user;
}
