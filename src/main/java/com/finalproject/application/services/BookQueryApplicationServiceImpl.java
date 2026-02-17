package com.finalproject.application.services;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.mapper.BookDataMapper;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.domain.entity.Author;
import com.finalproject.domain.entity.Book;
import com.finalproject.domain.exception.BookNotFoundException;
import com.finalproject.domain.valueobject.BookId;

public class BookQueryApplicationServiceImpl implements BookQueryApplicationService {
    private final BookDataMapper bookDataMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookQueryApplicationServiceImpl(BookRepository bookRepository,
                                           AuthorRepository authorRepository,
                                           BookDataMapper bookDataMapper) {
        this.bookDataMapper = bookDataMapper;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public FindBookResponse findBook(GetBookQuery query) {
        Book book = bookRepository.findById(new BookId(query.bookId()))
                .orElseThrow(() -> new BookNotFoundException("Book with bookId %d not found!".formatted(query.bookId())));
        Author author = authorRepository.findById(book.getAuthorId()).orElseThrow();
        return bookDataMapper.toFindBookResponse(book, author);
    }
}
