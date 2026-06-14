package com.elibrary.backend.repository;

import com.elibrary.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByIsbn(String isbn);

    List<Book> findByCategoryId(Integer categoryId);

    List<Book> findByTitleContainingIgnoreCase(String title);
}
