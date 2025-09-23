package com.shu.service.user.impl;

import com.shu.exceptions.UserException;
import com.shu.model.entity.User;
import com.shu.repository.UserRepository;
import com.shu.service.auth.JwtProvider;
import com.shu.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserServiceImpl
 *
 * Concrete implementation of UserService.
 *
 * Responsibilities:
 * - Interacts with UserRepository to fetch user data
 * - Validates user existence
 * - Uses JwtProvider to extract user info from JWT
 * - Relies on SecurityContextHolder to get the logged-in user
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return getUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new UserException("User not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
