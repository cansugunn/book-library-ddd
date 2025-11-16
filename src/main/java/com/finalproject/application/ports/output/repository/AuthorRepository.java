package com.finalproject.application.ports.output.repository;

import com.finalproject.domain.entity.Author;
import com.finalproject.domain.valueobject.AuthorId;
import com.finalproject.domain.valueobject.BookId;
import com.finalproject.domain.valueobject.Rating;
import com.finalproject.domain.valueobject.UserId;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(AuthorId id);

    void deleteById(AuthorId id);

    boolean hasMoreBooks(AuthorId authorId);

    Author save(Author author);

    boolean existsByNameAndSurname(String name, String surname);

    List<Author> findByName(String name);

    Optional<Author> findByBookId(BookId bookId);

    boolean hasMoreBooksExcluding(AuthorId authorId, BookId bookId);

    void deleteByNameAndSurname(String name, String surname);

    List<Author> findWhichUserHasAtLeastThreeBooksRatedOver(UserId userId, Rating rating);

    Optional<Author> findByNameAndSurname(String authorName, String authorSurname);
}
