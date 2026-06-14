package com.elibrary.backend.repository;

import com.elibrary.backend.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
    Optional<Branch> findByName(String name);
}
