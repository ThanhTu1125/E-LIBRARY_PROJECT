package com.elibrary.backend.repository;

import com.elibrary.backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByBookId(Integer bookId);

    List<Review> findByUserId(Integer userId);
}
