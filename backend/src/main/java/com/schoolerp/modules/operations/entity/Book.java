package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "library_books", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String isbn;

    private String category; // Science, Mathematics, Literature, Reference, History

    @Builder.Default
    private int totalCopies = 1;

    @Builder.Default
    private int availableCopies = 1;

    private String rackLocation; // e.g. Shelf C-4 / Rack 2

    private String barcodeTag;
}

