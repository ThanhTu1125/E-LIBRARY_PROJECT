package com.elibrary.backend.dto;

public record RegisterRequest(
        String username,
        String password,
        String email,
        String fullName) {
}