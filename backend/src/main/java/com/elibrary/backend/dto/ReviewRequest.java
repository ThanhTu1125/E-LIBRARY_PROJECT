package com.elibrary.backend.dto;

import org.springframework.lang.NonNull;

public record ReviewRequest(
        @NonNull Integer bookId,
        @NonNull Integer rating,
        String comment) {
}