package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class BookJpaMapper {

    public BookJpaEntity toEntity(Book book) {
        BookJpaEntity entity = new BookJpaEntity();
        if (book.getId() != null) {
            entity.setId(book.getId().getValue());
        }
        entity.setAuthorId(book.getAuthorId().getValue());
        entity.setTitle(book.getTitle());
        entity.setYear(book.getYear() == null ? null : book.getYear().getValue());
        entity.setNumberOfPages(book.getNumberOfPages() == null ? null : book.getNumberOfPages().getValue());
        entity.setCoverPath(book.getCover() == null ? null : book.getCover().getPath());
        entity.setAbout(book.getAbout());
        return entity;
    }

    public Book toDomain(BookJpaEntity entity) {
        return Book.Builder.newBuilder()
                .id(new BookId(entity.getId()))
                .author(new AuthorId(entity.getAuthorId()))
                .title(entity.getTitle())
                .year(entity.getYear() == null ? null : new Year(entity.getYear()))
                .numberOfPages(entity.getNumberOfPages() == null ? null : new NumberOfPages(entity.getNumberOfPages()))
                .cover(entity.getCoverPath() == null ? null : new Cover(entity.getCoverPath()))
                .about(entity.getAbout())
                .build();
    }
}
