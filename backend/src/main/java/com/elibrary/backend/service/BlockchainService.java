package com.elibrary.backend.service;

import com.elibrary.backend.model.BlockchainLedger;
import com.elibrary.backend.model.BlockchainTransaction;
import com.elibrary.backend.repository.BlockchainLedgerRepository;
import com.elibrary.backend.repository.BlockchainTransactionRepository;
import com.elibrary.backend.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final BlockchainLedgerRepository ledgerRepository;
    private final BlockchainTransactionRepository transactionRepository;

    /**
     * Ghi lại giao dịch vào Sổ cái Blockchain
     * 
     * @return Mã txHash sinh ra
     */
    @Transactional
    public String recordTransaction(String referenceType, Integer referenceId, String payload) {
        long timestamp = System.currentTimeMillis();

        // 1. Tìm Block trước đó để tạo chuỗi (Chaining)
        BlockchainLedger previousBlock = ledgerRepository.findTopByOrderByBlockIndexDesc().orElse(null);
        String previousHash = previousBlock != null ? previousBlock.getCurrentHash()
                : "0000000000000000000000000000000000000000000000000000000000000000"; // Genesis
        int newBlockIndex = previousBlock != null ? previousBlock.getBlockIndex() + 1 : 1;

        // 2. Tính toán Hash cho Giao dịch (txHash)
        String txRawData = referenceType + referenceId + payload + timestamp;
        String txHash = HashUtils.sha256(txRawData);

        // 3. Tính toán Hash cho Khối (Block Hash) kết hợp previousHash
        String blockRawData = previousHash + timestamp + txHash;
        String currentHash = HashUtils.sha256(blockRawData);

        // 4. Lưu Sổ cái (Ledger)
        BlockchainLedger newBlock = new BlockchainLedger();
        newBlock.setBlockIndex(newBlockIndex);
        newBlock.setTimestamp(timestamp);
        newBlock.setProofOfWork(1); // Hardcoded cho MVP
        newBlock.setPreviousHash(previousHash);
        newBlock.setCurrentHash(currentHash);
        ledgerRepository.save(newBlock);

        // 5. Lưu Giao dịch chi tiết (Transaction)
        BlockchainTransaction transaction = new BlockchainTransaction();
        transaction.setTxId(txHash);
        transaction.setLedger(newBlock);
        transaction.setReferenceType(referenceType);
        transaction.setReferenceId(referenceId);
        transaction.setDataPayload(payload);
        transactionRepository.save(transaction);

        return txHash;
    }
}