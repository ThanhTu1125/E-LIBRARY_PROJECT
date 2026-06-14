package com.elibrary.backend.repository;

import com.elibrary.backend.model.BlockchainTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlockchainTransactionRepository extends JpaRepository<BlockchainTransaction, String> {
    List<BlockchainTransaction> findByLedgerBlockIndex(Integer blockIndex);

    List<BlockchainTransaction> findByReferenceTypeAndReferenceId(String referenceType, Integer referenceId);
}
