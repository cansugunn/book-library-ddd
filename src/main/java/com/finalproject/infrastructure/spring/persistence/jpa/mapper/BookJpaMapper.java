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

        entity.setId(book.getId().getValue());
        entity.setAuthor(entityManager.getReference(AuthorJpaEntity.class, book.getAuthorId().getValue()));
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
                .id(new BookId(entity.getId()))
                .author(new AuthorId(entity.getAuthor().getId()))
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
