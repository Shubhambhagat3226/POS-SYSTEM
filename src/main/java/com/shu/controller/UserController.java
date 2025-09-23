package com.shu.controller;

import com.shu.constant.ApiPathConstant;
import com.shu.constant.OpenApiConstants;
import com.shu.mapper.UserMapper;
import com.shu.model.dto.UserDto;
import com.shu.model.entity.User;
import com.shu.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * REST controller to handle user-related endpoints.
 * Provides APIs for fetching user profile and user details by ID.
 */
@RestController
@RequestMapping(ApiPathConstant.USERS)
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for fetching user information")
@SecurityRequirement(name = OpenApiConstants.SECURITY_SCHEME_NAME) // Indicates these endpoints require bearer token
public class UserController {

    private final UserService userService;


    /**
     * Get the profile of the currently authenticated user.
     * The user is identified via the JWT token processed by the security filter.
     *
     * @return User profile (as DTO)
     */
    @GetMapping("/profile")
    @Operation(summary = "Get current user profile", description = "Returns the profile of the authenticated user.")
    @ApiResponse(responseCode = "200", description = "User profile retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT")
    public ResponseEntity<UserDto> getUserProfile() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    /**
     * Get user details by ID.
     * Access to this endpoint requires authentication.
     *
     * @param id User ID
     * @return User details (as DTO)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Fetches user details by user ID.")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable("id") Long id
    ) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }
}
