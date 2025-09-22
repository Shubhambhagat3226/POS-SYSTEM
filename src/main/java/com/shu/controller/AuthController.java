package com.shu.controller;

import com.shu.exceptions.UserException;
import com.shu.payload.request.LoginRequest;
import com.shu.payload.request.SignupRequest;
import com.shu.payload.response.AuthResponse;
import com.shu.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(
            @RequestBody SignupRequest request
            ) throws UserException {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody LoginRequest request
            ) throws UserException {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(request));
    }
}
