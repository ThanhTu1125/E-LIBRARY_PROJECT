package com.elibrary.backend.repository;

import com.elibrary.backend.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {
    Optional<BookCopy> findByBarcode(String barcode);

    List<BookCopy> findByBookId(Integer bookId);

    List<BookCopy> findByBranchIdAndStatus(Integer branchId, String status);
}
