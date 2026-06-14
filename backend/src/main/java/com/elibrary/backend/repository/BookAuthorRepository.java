package com.elibrary.backend.repository;

import com.elibrary.backend.model.BookAuthor;
import com.elibrary.backend.model.BookAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {
    List<BookAuthor> findByBookId(Integer bookId);

    List<BookAuthor> findByAuthorId(Integer authorId);
}
