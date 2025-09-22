package com.shu.payload.request;

import com.shu.domain.UserRole;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * SignupRequest DTO
 *
 * Represents the payload required to register a new user.
 * Used in /signup API.
 */
@Data
public class SignupRequest {

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    private String phone;

    @NotBlank
    private String password;

    private UserRole role;
}
