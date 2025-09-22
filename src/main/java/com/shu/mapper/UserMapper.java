package com.shu.mapper;

import com.shu.model.dto.UserDto;
import com.shu.model.entity.User;

/**
 * UserMapper
 *
 * Responsibility:
 * - Maps the User entity to a UserDto.
 * - Prevents sensitive information (like password) from being exposed to clients.
 *
 * Why use a mapper?
 * - Keeps your service layer clean.
 * - Provides a clear separation between the database entity and the API response.
 * - Ensures you never accidentally expose sensitive fields.
 */
public class UserMapper {

    /**
     * Converts a User entity into a UserDto.
     *
     * @param savedUser The User entity fetched or saved in the database
     * @return UserDto containing safe data for API responses
     */
    public static UserDto toDTO(User savedUser) {
        UserDto userDto = new UserDto();

        // Copy basic info
        userDto.setId(savedUser.getId());
        userDto.setFullName(savedUser.getFullName());
        userDto.setEmail(savedUser.getEmail());
        userDto.setRole(savedUser.getRole());
        userDto.setPhone(savedUser.getPhone());

        // Copy basic info
        userDto.setCreatedAt(savedUser.getCreatedAt());
        userDto.setUpdatedAt(savedUser.getUpdatedAt());
        userDto.setLastLogin(savedUser.getLastLogin());

        return userDto;
    }
}
