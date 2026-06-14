package com.elibrary.backend.service;

import com.elibrary.backend.model.Book;
import com.elibrary.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.lang.NonNull;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> getBooksByCategory(Integer categoryId) {
        return bookRepository.findByCategoryId(categoryId);
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
