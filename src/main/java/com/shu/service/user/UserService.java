package com.shu.service.user;

import com.shu.model.entity.User;

import java.util.List;

/**
 * UserService
 *
 * Defines user-related operations for the POS system.
 * Acts as an abstraction layer between controllers and repositories.
 *
 * Responsibilities:
 * - Retrieve user details using security context, email, or ID
 * - Provide currently authenticated user
 * - List all users
 */
public interface UserService {

    /**
     * Retrieves the currently authenticated user
     * from the Spring Security context.
     *
     * @return User currently logged in
     */
    User getCurrentUser();

    /**
     * Finds a user by email.
     *
     * @param email Email address
     * @return User entity
     */
    User getUserByEmail(String email);

    /**
     * Finds a user by ID.
     *
     * @param id User ID
     * @return User entity
     */
    User getUserById(Long id);

    /**
     * Retrieves all users in the system.
     *
     * @return List of User entities
     */
    List<User> getAllUsers();
}
