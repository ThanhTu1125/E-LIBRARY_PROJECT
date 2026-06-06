package com.elibrary.backend.repository;

import com.elibrary.backend.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Integer> {
    List<Fine> findByStatus(String status);
    List<Fine> findByBorrowingUserId(Integer userId);
}
