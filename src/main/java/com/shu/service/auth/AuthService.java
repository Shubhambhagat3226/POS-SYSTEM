package com.shu.service.auth;

import com.shu.exceptions.UserException;
import com.shu.payload.request.LoginRequest;
import com.shu.payload.request.SignupRequest;
import com.shu.payload.response.AuthResponse;

/**
 * AuthService
 *
 * Defines operations for user authentication and registration.
 * Responsible for handling signup and login logic at the service layer.
 */
public interface AuthService {

    /**
     * Registers a new user and returns authentication details including JWT.
     *
     * @param request SignupRequest containing user registration info
     * @return AuthResponse containing JWT and user information (without password)
     * @throws UserException if the email is already registered or role is restricted
     */
    AuthResponse signup(SignupRequest request) throws UserException;

    /**
     * Authenticates an existing user and returns authentication details including JWT.
     *
     * @param request LoginRequest containing user credentials
     * @return AuthResponse containing JWT and user information (without password)
     * @throws UserException if credentials are invalid or user does not exist
     */
    AuthResponse login(LoginRequest request) throws UserException;
}
