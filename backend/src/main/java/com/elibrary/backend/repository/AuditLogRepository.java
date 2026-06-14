package com.elibrary.backend.repository;

import com.elibrary.backend.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserId(Integer userId);

    List<AuditLog> findByActionContainingIgnoreCase(String action);
}
