package com.shu.service.auth.impl;

import com.shu.domain.UserRole;
import com.shu.exceptions.UserException;
import com.shu.mapper.UserMapper;
import com.shu.model.entity.User;
import com.shu.payload.request.LoginRequest;
import com.shu.payload.request.SignupRequest;
import com.shu.payload.response.AuthResponse;
import com.shu.repository.UserRepository;
import com.shu.service.auth.AuthService;
import com.shu.service.auth.CustomUserDetails;
import com.shu.service.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * AuthServiceImpl
 *
 * Professional-level authentication service implementation.
 *
 * Responsibilities:
 * - User registration (signup) with password hashing
 * - User login with password validation
 * - JWT token generation for stateless authentication
 * - SecurityContext management
 * - Updating last login timestamps
 *
 * Uses constructor-based dependency injection with Lombok's @RequiredArgsConstructor.
 */
@Service
@RequiredArgsConstructor // automatically config the object
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetails customUserDetails;

    /**
     * Registers a new user and returns an AuthResponse with JWT and user details.
     * Validates if the email is already registered and prevents admin role creation.
     *
     * Steps:
     * 1. Check if email already exists → throw UserException if yes
     * 2. Prevent creation of admin role by normal signup → throw UserException if attempted
     * 3. Create User entity and encode password
     * 4. Set timestamps for createdAt, updatedAt, and lastLogin
     * 5. Save user in database
     * 6. Authenticate user immediately for token generation
     * 7. Generate JWT token
     * 8. Map saved User to DTO and return AuthResponse
     *
     * @param request SignupRequest containing user registration info
     * @return AuthResponse containing JWT and user information
     * @throws UserException if email already exists or role is restricted
     */
    @Override
    public AuthResponse signup(SignupRequest request) throws UserException {
        User user = userRepository.findByEmail(request.getEmail());
        if (user != null) {
            throw new UserException("Email id already register!");
        }
        if (request.getRole().equals(UserRole.ROLE_ADMIN)) {
            throw new UserException("Role admin is not allowed!");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setFullName(request.getFullName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());
        newUser.setPhone(request.getPhone());
        newUser.setLastLogin(LocalDateTime.now());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);

        // Authenticate user for immediate token generation
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register Successfully!");
        authResponse.setUser(UserMapper.toDTO(savedUser));

        return authResponse;
    }

    /**
     * Authenticates a user using email and password and returns an AuthResponse with JWT.
     * Updates the last login timestamp for the user.
     *
     * Steps:
     * 1. Load user by email using CustomUserDetails
     * 2. Validate password
     * 3. Set authentication in SecurityContext
     * 4. Update last login timestamp
     * 5. Generate JWT token
     * 6. Return AuthResponse with user info and token
     *
     * @param request LoginRequest containing user credentials
     * @return AuthResponse containing JWT and user information
     * @throws UserException if the user does not exist or credentials are invalid
     */
    @Override
    public AuthResponse login(LoginRequest request) throws UserException {
        UserDetails userDetails = customUserDetails.loadUserByUsername(request.getEmail());


        if (userDetails == null) {
            throw new UserException("Email id doesn't exits: " + request.getEmail());
        }
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new UserException("Invalid Password!");
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = userRepository.findByEmail(request.getEmail());
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String jwt = jwtProvider.generateToken(auth);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("Login successfully");
        response.setUser(UserMapper.toDTO(user));
        return response;
    }

}
