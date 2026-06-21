package com.elibrary.backend.controller;

import com.elibrary.backend.dto.LoginRequest;
import com.elibrary.backend.dto.RegisterRequest;
import com.elibrary.backend.model.User;
import com.elibrary.backend.service.AuthService;
import com.elibrary.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
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

            String token = jwtUtils.generateToken(user.getUsername());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "user", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}