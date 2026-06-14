package com.elibrary.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blockchain_ledger")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BlockchainLedger {
    @Id
    @Column(name = "block_index")
    private Integer blockIndex;

    @Column(nullable = false)
    private Long timestamp;

    @Column(name = "proof_of_work", nullable = false)
    private Integer proofOfWork;

    @Column(name = "previous_hash", nullable = false, length = 64)
    private String previousHash;

    @Column(name = "current_hash", nullable = false, length = 64)
    private String currentHash;
}
