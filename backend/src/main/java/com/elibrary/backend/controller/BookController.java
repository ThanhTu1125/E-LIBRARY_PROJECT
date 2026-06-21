package com.elibrary.backend.controller;

import com.elibrary.backend.model.Book;
import com.elibrary.backend.model.Category;
import com.elibrary.backend.service.BookService;
import com.elibrary.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page, // Mặc định là trang 0
            @RequestParam(defaultValue = "10") int size) { // Mặc định 10 cuốn/trang
        return ResponseEntity.ok(bookService.getAllBooks(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookDetail(@PathVariable @NonNull Integer id) {
        bookService.incrementViewCount(id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Book>> searchBooks(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title, page, size));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Book>> getBooksByCategory(
            @PathVariable @NonNull Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getBooksByCategory(categoryId, page, size));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}