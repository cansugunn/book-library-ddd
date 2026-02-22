package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.AuthorJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.BookJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookJpaMapper {
    @PersistenceContext
    private final EntityManager entityManager;
    private final BookJpaRepository bookJpaRepository;

    public BookJpaEntity toEntity(Book book) {
        BookJpaEntity entity = Optional.ofNullable(book.getId())
                .map(BookId::getValue)
                .flatMap(bookJpaRepository::findById)
                .orElseGet(BookJpaEntity::new);

        entity.setId(Optional.ofNullable(book.getId())
                .map(BookId::getValue)
                .orElse(null));
        entity.setAuthor(Optional.ofNullable(book.getAuthorId())
                .map(AuthorId::getValue)
                .map(id -> entityManager.getReference(AuthorJpaEntity.class, id))
                .orElse(null));
        entity.setTitle(book.getTitle());
        entity.setYear(Optional.ofNullable(book.getYear())
                .map(Year::getValue)
                .orElse(null));
        entity.setNumberOfPages(Optional.ofNullable(book.getNumberOfPages())
                .map(NumberOfPages::getValue)
                .orElse(null));
        entity.setCoverPath(Optional.ofNullable(book.getCover())
                .map(Cover::getPath)
                .orElse(null));
        entity.setAbout(book.getAbout());

        return entity;
    }

    public Book toDomain(BookJpaEntity entity) {
        return Book.Builder.newBuilder()
                .id(Optional.ofNullable(entity.getId())
                        .map(BookId::new)
                        .orElse(null))
                .author(Optional.ofNullable(entity.getAuthor())
                        .map(AuthorJpaEntity::getId)
                        .map(AuthorId::new)
                        .orElse(null))
                .title(entity.getTitle())
                .year(Optional.ofNullable(entity.getYear())
                        .map(Year::new)
                        .orElse(null))
                .numberOfPages(Optional.ofNullable(entity.getNumberOfPages())
                        .map(NumberOfPages::new)
                        .orElse(null))
                .cover(Optional.ofNullable(entity.getCoverPath())
                        .map(Cover::new)
                        .orElse(null))
                .about(entity.getAbout())
                .build();
    }
}
