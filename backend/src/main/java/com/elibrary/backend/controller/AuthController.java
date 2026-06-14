package com.elibrary.backend.controller;

import com.elibrary.backend.dto.LoginRequest;
import com.elibrary.backend.dto.RegisterRequest;
import com.elibrary.backend.model.User;
import com.elibrary.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            // Chuyển dữ liệu từ DTO sang Entity
            User user = new User();
            user.setUsername(request.username());
            user.setPassword(request.password());
            user.setEmail(request.email());
            user.setFullName(request.fullName());

            User registeredUser = authService.register(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            User user = authService.login(request.username(), request.password());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}