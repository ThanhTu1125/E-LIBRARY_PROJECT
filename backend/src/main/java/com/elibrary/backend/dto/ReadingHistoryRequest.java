package com.elibrary.backend.dto;

import org.springframework.lang.NonNull;

public record ReadingHistoryRequest(
        @NonNull Integer bookId,
        @NonNull Integer lastReadPage) {
}