package com.elibrary.backend.service;

import com.elibrary.backend.model.Book;
import com.elibrary.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // Lấy toàn bộ sách (Mặc định sắp xếp theo Tên sách A-Z)
    public Page<Book> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return bookRepository.findAll(pageable);
    }

    // Tìm kiếm sách theo tên có phân trang
    public Page<Book> searchBooksByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    // Lọc sách theo danh mục có phân trang
    public Page<Book> getBooksByCategory(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return bookRepository.findByCategoryId(categoryId, pageable);
    }

    public Book getBookById(@NonNull Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + id));
    }

    public Book incrementViewCount(@NonNull Integer id) {
        Book book = getBookById(id);
        Integer currentView = book.getViewCount();
        book.setViewCount(currentView == null ? 1 : currentView + 1);
        return bookRepository.save(book);
    }
}