package com.elibrary.backend.repository;

import com.elibrary.backend.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Integer> {
    List<Borrowing> findByUserId(Integer userId);

    List<Borrowing> findByStatus(String status);

    List<Borrowing> findByBookCopyIdAndStatus(Integer bookCopyId, String status);
}
