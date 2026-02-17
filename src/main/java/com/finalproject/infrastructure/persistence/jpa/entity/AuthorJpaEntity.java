package com.finalproject.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;

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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
}
