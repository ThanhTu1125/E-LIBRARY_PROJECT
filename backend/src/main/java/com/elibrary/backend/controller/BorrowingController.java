package com.elibrary.backend.controller;

import com.elibrary.backend.dto.BorrowingRequest;
import com.elibrary.backend.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;
import java.security.Principal;

@RestController
@RequestMapping("/api/borrowings")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping
    // Tiêm thêm Principal vào đây. Spring Security sẽ tự động lấy từ Token JWT!
    public ResponseEntity<?> borrowBook(@RequestBody BorrowingRequest request, Principal principal) {
        try {
            // principal.getName() chính là username của người đang cầm thẻ!
            return ResponseEntity.ok(borrowingService.borrowBook(request, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable @NonNull Integer id) {
        try {
            return ResponseEntity.ok(borrowingService.returnBook(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}