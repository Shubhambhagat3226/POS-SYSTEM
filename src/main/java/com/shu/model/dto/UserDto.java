package com.shu.model.dto;

import com.shu.domain.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for User entity.
 *
 * Represents user information returned in API responses.
 * Excludes sensitive fields like password.
 *
 * Purpose:
 * - Used to transfer user-related data across layers
 *   (Controller ↔ Service ↔ Client).
 * - Prevents exposing JPA entity directly in APIs.
 */
@Data
public class UserDto {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private UserRole role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
}
