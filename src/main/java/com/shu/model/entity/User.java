package com.shu.model.entity;

import com.shu.domain.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;


/**
 *
 * USER ENTITY:
 * Represents a user in the POS system.
 * Stores authentication, role-based access, and optional store association.
 *
 * Fields:
 * - id          : Unique identifier for the user (Primary Key).
 * - fullName    : User's full name (cannot be null).
 * - email       : Unique email address used for login (validated format).
 * - phone       : Optional contact number.
 * - store      : Associated store entity (if user is an employee, cashier, or store admin).
 * - role        : UserRole defining permissions (e.g., ROLE_ADMIN, ROLE_CASHIER, etc.).
 * - password    : Hashed password for authentication.
 * - createdAt   : Timestamp when the user was created.
 * - updatedAt   : Timestamp of the last update to user details.
 * - lastLogin   : Timestamp of the user's last login.
 *
 * Annotations:
 * - @Entity           : Marks this class as a JPA entity.
 * - @Id               : Primary key of the entity.
 * - @Column           : Database column configuration.
 * - @ManyToOne         : Maps multiple users to a single store (many employees per store).
 * - @Enumerated       : Stores enum as STRING in DB.
 * - @Email            : Validates email format.
 * - Lombok annotations: @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @EqualsAndHashCode
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    @ManyToOne
    private Store store;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
}
