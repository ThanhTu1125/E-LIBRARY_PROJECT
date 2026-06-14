package com.elibrary.backend.dto;

import org.springframework.lang.NonNull;

public record BorrowingRequest(
        @NonNull Integer userId,
        @NonNull Integer bookCopyId,
        @NonNull Integer durationDays) {
}