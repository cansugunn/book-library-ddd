package com.finalproject.infrastructure.spring.persistence.jpa.adapter;

import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.domain.entity.Author;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.AuthorJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.SpringDataAuthorRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaAuthorRepositoryAdapter implements AuthorRepository {
    private final SpringDataAuthorRepository repository;

    public JpaAuthorRepositoryAdapter(SpringDataAuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Author> findById(AuthorId id) {
        return repository.findById(id.getValue()).map(this::toDomain);
    }

    @Override
    public void deleteById(AuthorId id) {
        repository.deleteById(id.getValue());
    }

    @Override
    public boolean hasMoreBooks(AuthorId authorId) {
        return repository.hasMoreBooks(authorId.getValue());
    }

    @Override
    public Author save(Author author) {
        AuthorJpaEntity saved = repository.save(toEntity(author));
        return toDomain(saved);
    }

    @Override
    public boolean existsByNameAndSurname(String name, String surname) {
        return repository.existsByNameAndSurname(name, surname);
    }

    @Override
    public List<Author> findByName(String name) {
        return repository.findByNameIgnoreCase(name).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Author> findByBookId(BookId bookId) {
        return repository.findByBookId(bookId.getValue()).map(this::toDomain);
    }

    @Override
    public boolean hasMoreBooksExcluding(AuthorId authorId, BookId bookId) {
        return repository.hasMoreBooksExcluding(authorId.getValue(), bookId.getValue());
    }

    @Override
    public void deleteByNameAndSurname(String name, String surname) {
        repository.deleteByNameAndSurname(name, surname);
    }

    @Override
    public List<Author> findWhichUserHasAtLeastThreeBooksRatedOver(UserId userId, Rating rating) {
        return List.of();
    }

    @Override
    public Optional<Author> findByNameAndSurname(String authorName, String authorSurname) {
        return repository.findByNameAndSurname(authorName, authorSurname).map(this::toDomain);
    }

    private AuthorJpaEntity toEntity(Author author) {
        AuthorJpaEntity entity = new AuthorJpaEntity();
        if (author.getId() != null) {
            entity.setId(author.getId().getValue());
        }
        entity.setName(author.getName());
        entity.setSurname(author.getSurname());
        entity.setWebsite(author.getWebsite() == null ? null : author.getWebsite().getUrl());
        return entity;
    }

    private Author toDomain(AuthorJpaEntity entity) {
        return new Author.Builder()
                .id(new AuthorId(entity.getId()))
                .name(entity.getName())
                .surname(entity.getSurname())
                .website(entity.getWebsite() == null ? null : new Website(entity.getWebsite()))
                .build();
    }
}
