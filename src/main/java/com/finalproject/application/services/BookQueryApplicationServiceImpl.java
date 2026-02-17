package com.finalproject.application.services;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.dto.book.query.SearchBooksQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.mapper.BookDataMapper;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.domain.entity.Author;
import com.finalproject.domain.entity.Book;
import com.finalproject.domain.exception.BookNotFoundException;
import com.finalproject.domain.valueobject.BookId;

import java.util.function.Function;

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
    public FindBookResponse find(GetBookQuery query) {
        Book book = bookRepository.findById(new BookId(query.bookId()))
                .orElseThrow(() -> new BookNotFoundException("Book with bookId %d not found!"
                        .formatted(query.bookId())));
        Author author = authorRepository.findById(book.getAuthorId()).orElseThrow();
        return bookDataMapper.toFindBookResponse(book, author);
    }

    @Override
    public PageResult<FindBookResponse> findAll(SearchBooksQuery query) {
        if (query.title() == null) {
            return bookRepository.findAll(query.pageQuery())
                    .map(toFindBookResponseFunction());
        }
        return bookRepository.searchByTitle(query.title(), query.pageQuery())
                .map(toFindBookResponseFunction());
    }

    //todo decouple the mapper
    private Function<Book, FindBookResponse> toFindBookResponseFunction() {
        return book -> {
            //todo n+1 problem
            Author author = authorRepository.findById(book.getAuthorId()).orElseThrow();
            return bookDataMapper.toFindBookResponse(book, author);
        };
    }
}
