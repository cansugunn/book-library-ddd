package com.finalproject.infrastructure.spring.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books_read_model")
@Getter
@Setter
@NoArgsConstructor
public class BookReadJpaEntity {
    @Id
    private Integer id;

    @Column(name = "author_id", nullable = false)
    private Integer authorId;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "author_surname", nullable = false)
    private String authorSurname;

    @Column(name = "author_website")
    private String authorWebsite;

    @Column(nullable = false)
    private String title;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "number_of_pages", nullable = false)
    private Integer numberOfPages;

    @Column(name = "cover_path")
    private String coverPath;

    private String about;
}
