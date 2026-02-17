package com.finalproject.application.mapper;

import com.finalproject.application.dto.CreateBookResponse;
import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.UpdateBookResponse;
import com.finalproject.application.dto.book.command.CreateBookCommand;
import com.finalproject.application.dto.book.command.UpdateBookCommand;
import com.finalproject.domain.entity.Author;
import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.*;

import java.util.Optional;

public class BookDataMapper {
    public Book toBook(CreateBookCommand command, Author author) {
        return new Book(
                null,
                author.getId(),
                command.title(),
                new Year(command.year()),
                new NumberOfPages(command.numberOfPages()),
                new Cover(command.coverPath()),
                command.about());
    }

    public CreateBookResponse toCreateBookResponse(Book book, Author author) {
        return new CreateBookResponse.Builder()
                .authorId(Optional.ofNullable(author.getId())
                        .map(AuthorId::getValue)
                        .orElse(null))
                .authorName(author.getName())
                .authorSurname(author.getSurname())
                .bookId(Optional.ofNullable(book.getId())
                        .map(BookId::getValue)
                        .orElse(null))
                .title(book.getTitle())
                .year(Optional.ofNullable(book.getYear())
                        .map(Year::getValue)
                        .orElse(null))
                .numberOfPages(Optional.ofNullable(book.getNumberOfPages())
                        .map(NumberOfPages::getValue)
                        .orElse(null))
                .coverPath(Optional.ofNullable(book.getCover())
                        .map(Cover::getPath)
                        .orElse(null))
                .about(book.getAbout())
                .build();
    }

    public Book toBook(UpdateBookCommand command, Author author) {
        return new Book(
                new BookId(command.bookId()),
                author.getId(),
                command.title(),
                new Year(command.year()),
                new NumberOfPages(command.numberOfPages()),
                new Cover(command.coverPath()),
                command.about());
    }

    public UpdateBookResponse toUpdateBookResponse(Book book, Author author) {
        return new UpdateBookResponse.Builder()
                .authorId(Optional.ofNullable(author.getId())
                        .map(AuthorId::getValue)
                        .orElse(null))
                .authorName(author.getName())
                .authorSurname(author.getSurname())
                .bookId(Optional.ofNullable(book.getId())
                        .map(BookId::getValue)
                        .orElse(null))
                .title(book.getTitle())
                .year(Optional.ofNullable(book.getYear())
                        .map(Year::getValue)
                        .orElse(null))
                .numberOfPages(Optional.ofNullable(book.getNumberOfPages())
                        .map(NumberOfPages::getValue)
                        .orElse(null))
                .coverPath(Optional.ofNullable(book.getCover())
                        .map(Cover::getPath)
                        .orElse(null))
                .about(book.getAbout())
                .build();
    }

    public FindBookResponse toFindBookResponse(Book book,
                                               Author author) {
        return new FindBookResponse.Builder()
                .authorId(Optional.ofNullable(author.getId())
                        .map(AuthorId::getValue)
                        .orElse(null))
                .authorName(author.getName())
                .authorSurname(author.getSurname())
                .bookId(Optional.ofNullable(book.getId())
                        .map(BookId::getValue)
                        .orElse(null))
                .title(book.getTitle())
                .year(Optional.ofNullable(book.getYear())
                        .map(Year::getValue)
                        .orElse(null))
                .numberOfPages(Optional.ofNullable(book.getNumberOfPages())
                        .map(NumberOfPages::getValue)
                        .orElse(null))
                .coverPath(Optional.ofNullable(book.getCover())
                        .map(Cover::getPath)
                        .orElse(null))
                .about(book.getAbout())
                .build();

    }


}
