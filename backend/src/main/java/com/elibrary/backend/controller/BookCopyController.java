package com.elibrary.backend.controller;

import com.elibrary.backend.model.BookCopy;
import com.elibrary.backend.service.BookCopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;

import java.util.List;

@RestController
@RequestMapping("/api/copies")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BookCopyController {

    private final BookCopyService bookCopyService;

    @PostMapping("/import")
    public ResponseEntity<?> importCopies(
            @RequestParam @NonNull Integer bookId,
            @RequestParam @NonNull Integer branchId,
            @RequestParam int quantity) {
        try {
            List<BookCopy> copies = bookCopyService.importBookCopies(bookId, branchId, quantity);
            return ResponseEntity.ok(copies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}