package com.trasexp.presentation.controller;

import com.trasexp.application.service.AuthService;
import com.trasexp.presentation.dto.AuthResponse;
import com.trasexp.presentation.dto.ErrorResponse;
import com.trasexp.presentation.dto.LoginRequest;
import com.trasexp.presentation.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Registration failed", List.of(e.getMessage() != null ? e.getMessage() : "Unknown error")));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Login error: " + e.getMessage());

            if (e.getMessage() != null && e.getMessage().contains("Invalid email or password")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Invalid email or Password",
                                List.of("Please check your credentials and try again")));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error during login", List.of(e.getMessage() != null ? e.getMessage() : "Unknown error")));
        }
    }
}

