package com.elibrary.backend.repository;

import com.elibrary.backend.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    List<Recommendation> findByUserIdOrderByScoreDesc(Integer userId);
    void deleteByUserId(Integer userId);
}
