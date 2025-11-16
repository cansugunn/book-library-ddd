package com.finalproject.application.ports.output.repository;

import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.BookId;
import com.finalproject.domain.valueobject.UserBookStateId;
import com.finalproject.domain.valueobject.UserId;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    boolean exists(BookId bookId);

    Book save(Book book);

    Book update(Book book);

    void delete(BookId bookId);

    Optional<Book> findById(BookId bookId);

    List<Book> findBooksWithoutUserBookStateRecords(UserId userId);
}
