package com.elibrary.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blockchain_transactions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BlockchainTransaction {
    @Id
    @Column(name = "tx_id", length = 64)
    private String txId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_index", nullable = false)
    private BlockchainLedger ledger;

    @Column(name = "reference_type", nullable = false, length = 50)
    private String referenceType;

    @Column(name = "reference_id", nullable = false)
    private Integer referenceId;

    @Column(name = "data_payload", nullable = false, columnDefinition = "text")
    private String dataPayload;
}
