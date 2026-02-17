package com.finalproject.infrastructure.spring.persistence.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class BookJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "author_id", nullable = false)
    private Integer authorId;

    @Column(nullable = false)
    private String title;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "number_of_pages", nullable = false)
    private Integer numberOfPages;

    @Column(name = "cover_path")
    private String coverPath;

    private String about;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getAuthorId() { return authorId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public Integer getNumberOfPages() { return numberOfPages; }
    public void setNumberOfPages(Integer numberOfPages) { this.numberOfPages = numberOfPages; }
    public String getCoverPath() { return coverPath; }
    public void setCoverPath(String coverPath) { this.coverPath = coverPath; }
    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }
}
