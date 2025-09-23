package com.shu.controller;

import com.shu.constant.ApiPathConstant;
import com.shu.exceptions.UserException;
import com.shu.payload.request.LoginRequest;
import com.shu.payload.request.SignupRequest;
import com.shu.payload.response.AuthResponse;
import com.shu.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController
 *
 * REST controller responsible for handling authentication-related requests
 * such as user registration (signup) and login.
 *
 * Exposes endpoints under the "/auth" base path.
 */
@RestController
@RequestMapping(ApiPathConstant.AUTH)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user signup and login")
public class AuthController {

    private final AuthService authService;


    /**
     * Registers a new user.
     *
     * @param request signup request containing user details
     * @return JWT authentication response
     */
    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token.")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid signup data (e.g., invalid role)")
    @ApiResponse(responseCode = "409", description = "Conflict - Email is already registered")
    public ResponseEntity<AuthResponse> signupHandler(
            @RequestBody SignupRequest request
            ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signup(request));
    }

    /**
     * Authenticates an existing user.
     *
     * @param request login request containing email and password
     * @return JWT authentication response
     * @throws UserException if authentication fails
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates an existing user and returns a JWT token.")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid password")
    @ApiResponse(responseCode = "404", description = "Not Found - User with the given email does not exist")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody LoginRequest request
            ) throws UserException {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(request));
    }
}
