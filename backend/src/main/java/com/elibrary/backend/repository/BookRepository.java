package com.elibrary.backend.repository;

import com.elibrary.backend.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByIsbn(String isbn);

    Page<Book> findByCategoryId(Integer categoryId, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}