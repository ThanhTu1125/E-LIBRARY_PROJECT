package com.elibrary.backend.service;

import com.elibrary.backend.model.User;
import com.elibrary.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Tên tài khoản đã tồn tại!");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if (!user.isStatus()) {
            throw new RuntimeException("Tài khoản của bro đã bị khóa!");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }

        return user;
    }
}