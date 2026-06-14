package com.elibrary.backend.repository;

import com.elibrary.backend.model.BlockchainLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BlockchainLedgerRepository extends JpaRepository<BlockchainLedger, Integer> {
    Optional<BlockchainLedger> findByCurrentHash(String currentHash);

    Optional<BlockchainLedger> findFirstByOrderByBlockIndexDesc();
}
