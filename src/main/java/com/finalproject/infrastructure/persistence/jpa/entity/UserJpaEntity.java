package com.finalproject.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "userinfo")
public class UserJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "type", nullable = false)
    private Integer type;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
}
