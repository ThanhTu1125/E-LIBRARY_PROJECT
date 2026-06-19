package com.elibrary.backend.controller;

import com.elibrary.backend.model.Fine;
import com.elibrary.backend.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Fine>> getFinesByUser(@PathVariable @NonNull Integer userId) {
        return ResponseEntity.ok(fineService.getFinesByUser(userId));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payFine(@PathVariable @NonNull Integer id) {
        try {
            return ResponseEntity.ok(fineService.payFine(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}