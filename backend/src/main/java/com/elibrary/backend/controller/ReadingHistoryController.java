package com.elibrary.backend.controller;

import com.elibrary.backend.dto.ReadingHistoryRequest;
import com.elibrary.backend.service.ReadingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReadingHistoryController {

    private final ReadingHistoryService historyService;

    @PostMapping
    public ResponseEntity<?> updateHistory(@RequestBody ReadingHistoryRequest request, Principal principal) {
        try {
            return ResponseEntity.ok(historyService.updateHistory(request, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getMyHistory(Principal principal) {
        try {
            return ResponseEntity.ok(historyService.getMyHistory(principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}