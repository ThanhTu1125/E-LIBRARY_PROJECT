package com.elibrary.backend.dto;

import org.springframework.lang.NonNull;

public record BorrowingRequest(
                @NonNull Integer bookCopyId,
                @NonNull Integer durationDays) {
}