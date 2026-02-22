package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookReadJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookReadJpaMapper {
    public Book toDomain(BookReadJpaEntity entity) {
        return Book.Builder.newBuilder()
                .id(Optional.ofNullable(entity.getId())
                        .map(BookId::new)
                        .orElse(null))
                .author(Optional.ofNullable(entity.getAuthorId())
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
