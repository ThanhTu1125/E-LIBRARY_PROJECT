package com.elibrary.backend.repository;

import com.elibrary.backend.model.BlockchainTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BlockchainTransactionRepository extends JpaRepository<BlockchainTransaction, String> {
    List<BlockchainTransaction> findByLedgerBlockIndex(Integer blockIndex);
    List<BlockchainTransaction> findByReferenceTypeAndReferenceId(String referenceType, Integer referenceId);
}
