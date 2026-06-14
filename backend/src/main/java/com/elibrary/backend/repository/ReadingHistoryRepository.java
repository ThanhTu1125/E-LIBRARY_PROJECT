package com.elibrary.backend.repository;

import com.elibrary.backend.model.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, Integer> {
    List<ReadingHistory> findByUserId(Integer userId);

    Optional<ReadingHistory> findByUserIdAndBookId(Integer userId, Integer bookId);
}
