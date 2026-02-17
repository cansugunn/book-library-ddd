package com.finalproject.infrastructure.spring.persistence.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class CommentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_book_state_id", nullable = false)
    private UserBookStateJpaEntity userBookState;

    @Column(name = "value", nullable = false)
    private String value;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public UserBookStateJpaEntity getUserBookState() { return userBookState; }
    public void setUserBookState(UserBookStateJpaEntity userBookState) { this.userBookState = userBookState; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
