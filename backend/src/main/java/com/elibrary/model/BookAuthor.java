package com.elibrary.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_authors")
@IdClass(BookAuthorId.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BookAuthor {
    @Id
    @Column(name = "book_id")
    private Integer bookId;

    @Id
    @Column(name = "author_id")
    private Integer authorId;
}
