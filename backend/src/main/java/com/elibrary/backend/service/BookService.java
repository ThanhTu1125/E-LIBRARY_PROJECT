package com.elibrary.backend.service;

import com.elibrary.backend.model.Book;
import com.elibrary.backend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> getBooksByCategory(Integer categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    public Book getBookById(Integer id) {
        Book book = getBookById(id);
        Integer currentView = book.getViewCount();
        book.setViewCount(currentView == null ? 1 : currentView + 1);
        return bookRepository.save(book);
    }

    public Book incrementViewCount(Integer id) {
        Book book = getBookById(id);
        book.setViewCount(book.getViewCount() + 1);
        return bookRepository.save(book);
    }
}
