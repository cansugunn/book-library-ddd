package com.finalproject.infrastructure.spring.persistence.jpa.adapter;

import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.domain.entity.Author;
import com.finalproject.domain.valueobject.AuthorId;
import com.finalproject.domain.valueobject.BookId;
import com.finalproject.domain.valueobject.Rating;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.infrastructure.spring.persistence.jpa.mapper.AuthorJpaMapper;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.AuthorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorJpaRepositoryAdapter implements AuthorRepository {
    private final AuthorJpaRepository authorJpaRepository;
    private final AuthorJpaMapper authorJpaMapper;

    @Override
    public Optional<Author> findById(AuthorId id) {
        return authorJpaRepository.findById(id.getValue()).map(authorJpaMapper::toDomain);
    }

    @Override
    public void deleteById(AuthorId id) {
        authorJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean hasMoreBooks(AuthorId authorId) {
        return authorJpaRepository.hasMoreBooks(authorId.getValue());
    }

    @Override
    public Author save(Author author) {
        return authorJpaMapper.toDomain(
                authorJpaRepository.save(
                        authorJpaMapper.toEntity(author)));
    }

    @Override
    public boolean existsByNameAndSurname(String name, String surname) {
        return authorJpaRepository.existsByNameAndSurname(name, surname);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorJpaRepository.findByNameIgnoreCase(name)
                .stream()
                .map(authorJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Author> findByBookId(BookId bookId) {
        return authorJpaRepository.findByBookId(bookId.getValue()).map(authorJpaMapper::toDomain);
    }

    @Override
    public boolean hasMoreBooksExcluding(AuthorId authorId, BookId bookId) {
        return authorJpaRepository.hasMoreBooksExcluding(authorId.getValue(), bookId.getValue());
    }

    @Override
    public List<Author> findWhichUserHasAtLeastThreeBooksRatedOver(UserId userId, Rating rating) {
        return authorJpaRepository
                .findWhichUserHasAtLeastThreeBooksRatedOver(userId.getValue(), rating.getValue())
                .stream()
                .map(authorJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Author> findByNameAndSurname(String authorName, String authorSurname) {
        return authorJpaRepository.findByNameAndSurname(authorName, authorSurname).map(authorJpaMapper::toDomain);
    }
}
