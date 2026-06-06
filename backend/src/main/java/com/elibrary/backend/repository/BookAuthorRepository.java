package com.elibrary.backend.repository;

import com.elibrary.backend.model.BookAuthor;
import com.elibrary.backend.model.BookAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {
    List<BookAuthor> findByBookId(Integer bookId);
    List<BookAuthor> findByAuthorId(Integer authorId);
}
