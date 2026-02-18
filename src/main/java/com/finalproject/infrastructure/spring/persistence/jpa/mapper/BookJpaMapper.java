package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.AuthorJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.JpaAuthorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookJpaMapper {
    private final JpaAuthorRepository authorRepository;

    public BookJpaMapper(JpaAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public BookJpaEntity toEntity(Book book) {
        BookJpaEntity entity = new BookJpaEntity();
        if (book.getId() != null) {
            entity.setId(book.getId().getValue());
        }

        AuthorJpaEntity author = Optional.ofNullable(book.getAuthorId())
                .map(AuthorId::getValue)
                .map(authorRepository::getReferenceById)
                .orElseThrow(() -> new IllegalStateException("Book author id must not be null"));

        entity.setAuthor(author);
        entity.setTitle(book.getTitle());
        entity.setYear(book.getYear() == null ? null : book.getYear().getValue());
        entity.setNumberOfPages(book.getNumberOfPages() == null ? null : book.getNumberOfPages().getValue());
        entity.setCoverPath(book.getCover() == null ? null : book.getCover().getPath());
        entity.setAbout(book.getAbout());
        return entity;
    }

    public Book toDomain(BookJpaEntity entity) {
        AuthorId authorId = Optional.ofNullable(entity.getAuthor())
                .map(AuthorJpaEntity::getId)
                .map(AuthorId::new)
                .orElseThrow(() -> new IllegalStateException("Book entity must have an author"));

        return Book.Builder.newBuilder()
                .id(new BookId(entity.getId()))
                .author(authorId)
                .title(entity.getTitle())
                .year(entity.getYear() == null ? null : new Year(entity.getYear()))
                .numberOfPages(entity.getNumberOfPages() == null ? null : new NumberOfPages(entity.getNumberOfPages()))
                .cover(entity.getCoverPath() == null ? null : new Cover(entity.getCoverPath()))
                .about(entity.getAbout())
                .build();
    }
}
