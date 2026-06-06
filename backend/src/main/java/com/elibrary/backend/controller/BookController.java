package com.elibrary.backend.controller;

import com.elibrary.backend.model.Book;
import com.elibrary.backend.model.Category;
import com.elibrary.backend.service.BookService;
import com.elibrary.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    // Lấy toàn bộ danh sách sách
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // Chi tiết sách và tự động tăng lượt xem (View Count)
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookDetail(@PathVariable Integer id) {
        bookService.incrementViewCount(id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // Tìm kiếm sách theo tiêu đề chữ hoa/chữ thường bách phát bách trúng
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    // Lọc sách theo danh mục thể loại
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(bookService.getBooksByCategory(categoryId));
    }

    // Lấy tất cả thể loại sách số để hiển thị lên Menu thanh điều hướng
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
