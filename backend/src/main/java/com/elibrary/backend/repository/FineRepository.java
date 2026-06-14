package com.elibrary.backend.repository;

import com.elibrary.backend.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Integer> {
    List<Fine> findByStatus(String status);

    List<Fine> findByBorrowingUserId(Integer userId);
}
