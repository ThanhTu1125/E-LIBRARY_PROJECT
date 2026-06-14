package com.elibrary.backend.controller;

import com.elibrary.backend.dto.BorrowingRequest;
import com.elibrary.backend.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowings")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    // API xử lý luồng mượn sách
    @PostMapping
    public ResponseEntity<?> borrowBook(@RequestBody BorrowingRequest request) {
        try {
            return ResponseEntity.ok(borrowingService.borrowBook(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}