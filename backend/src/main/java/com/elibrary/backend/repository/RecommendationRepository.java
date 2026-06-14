package com.elibrary.backend.repository;

import com.elibrary.backend.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    List<Recommendation> findByUserIdOrderByScoreDesc(Integer userId);

    void deleteByUserId(Integer userId);
}
