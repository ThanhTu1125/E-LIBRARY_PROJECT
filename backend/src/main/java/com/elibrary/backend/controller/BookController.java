package com.elibrary.backend.controller;

import com.elibrary.backend.model.Book;
import com.elibrary.backend.model.Category;
import com.elibrary.backend.service.BookService;
import com.elibrary.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor // Tự động tạo Constructor thay cho @Autowired
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // THÊM @NonNull VÀO TRƯỚC Integer id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookDetail(@PathVariable @NonNull Integer id) {
        bookService.incrementViewCount(id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    // THÊM @NonNull VÀO TRƯỚC Integer categoryId cho đồng bộ
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable @NonNull Integer categoryId) {
        return ResponseEntity.ok(bookService.getBooksByCategory(categoryId));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}