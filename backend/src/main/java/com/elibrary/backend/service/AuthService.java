package com.elibrary.backend.service;

import com.elibrary.backend.model.User;
import com.elibrary.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // Logic Đăng ký tài khoản mới
    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Tên tài khoản đã tồn tại!");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        // Tạm thời lưu thô mật khẩu, giai đoạn sau sẽ tích hợp BCryptPasswordEncoder
        return userRepository.save(user);
    }

    // Logic Đăng nhập hệ thống
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        // Kiểm tra trạng thái tài khoản
        if (!user.isStatus()) {
            throw new RuntimeException("Tài khoản của bro đã bị khóa!");
        }

        // Tạm thời so sánh thô, giai đoạn bảo mật sau sẽ khớp mã hóa BCrypt
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }

        return user;
    }
}
