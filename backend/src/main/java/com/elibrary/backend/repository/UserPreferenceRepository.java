package com.elibrary.backend.repository;

import com.elibrary.backend.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> {
    List<UserPreference> findByUserId(Integer userId);

    List<UserPreference> findByUserIdAndCategoryId(Integer userId, Integer categoryId);
}
