package com.elibrary.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_copies")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(nullable = false, unique = true, length = 100)
    private String barcode;

    @Column(nullable = false, length = 30)
    private String status = "AVAILABLE";
}
