package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.AuthorJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookReadJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookReadJpaMapper {
    public Book toDomain(BookReadJpaEntity entity) {
        return Book.Builder.newBuilder()
                .id(new BookId(entity.getId()))
                .author(new AuthorId(entity.getAuthorId()))
                .title(entity.getTitle())
                .year(Optional.ofNullable(entity.getYear()).map(Year::new).orElse(null))
                .numberOfPages(Optional.ofNullable(entity.getNumberOfPages()).map(NumberOfPages::new).orElse(null))
                .cover(Optional.ofNullable(entity.getCoverPath()).map(Cover::new).orElse(null))
                .about(entity.getAbout())
                .build();
    }

    public BookReadJpaEntity toReadEntity(BookJpaEntity book, AuthorJpaEntity author) {
        BookReadJpaEntity entity = new BookReadJpaEntity();
        entity.setId(book.getId());
        entity.setAuthorId(author.getId());
        entity.setAuthorName(author.getName());
        entity.setAuthorSurname(author.getSurname());
        entity.setAuthorWebsite(author.getWebsite());
        entity.setTitle(book.getTitle());
        entity.setYear(book.getYear());
        entity.setNumberOfPages(book.getNumberOfPages());
        entity.setCoverPath(book.getCoverPath());
        entity.setAbout(book.getAbout());
        return entity;
    }
}
