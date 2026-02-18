package com.finalproject.infrastructure.spring.persistence.jpa.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
public class AuthorJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    private String website;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<BookJpaEntity> books = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public List<BookJpaEntity> getBooks() { return books; }
    public void setBooks(List<BookJpaEntity> books) { this.books = books; }

    public void addBook(BookJpaEntity book) {
        books.add(book);
        book.setAuthor(this);
    }

    public void removeBook(BookJpaEntity book) {
        books.remove(book);
        book.setAuthor(null);
    }
}
