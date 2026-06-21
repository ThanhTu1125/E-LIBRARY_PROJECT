package com.elibrary.backend.controller;

import com.elibrary.backend.dto.ReviewRequest;
import com.elibrary.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest request, Principal principal) {
        try {
            return ResponseEntity.ok(reviewService.addReview(request, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getBookReviews(@PathVariable Integer bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBook(bookId));
    }
}