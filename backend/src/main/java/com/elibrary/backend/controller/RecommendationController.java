package com.elibrary.backend.controller;

import com.elibrary.backend.model.Recommendation;
import com.elibrary.backend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    // API ép hệ thống tính toán lại gợi ý ngay lập tức
    @PostMapping("/generate")
    public ResponseEntity<?> generateRecommendations(Principal principal) {
        try {
            recommendationService.generateRecommendationsForUser(principal.getName());
            return ResponseEntity.ok("Đã chạy thành công thuật toán AI gợi ý sách!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API lấy danh sách sách được gợi ý
    @GetMapping
    public ResponseEntity<List<Recommendation>> getMyRecommendations(Principal principal) {
        return ResponseEntity.ok(recommendationService.getMyRecommendations(principal.getName()));
    }
}