package com.finalproject.application.services;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.mapper.BookDataMapper;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.application.ports.output.fms.FileManagementService;
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
    private final FileManagementService fileManagementService;

    public BookQueryApplicationServiceImpl(BookRepository bookRepository,
                                           AuthorRepository authorRepository,
                                           BookDataMapper bookDataMapper,
                                           FileManagementService fileManagementService) {
        this.bookDataMapper = bookDataMapper;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.fileManagementService = fileManagementService;
    }

    @Override
    public FindBookResponse findBook(GetBookQuery query) {
        Book book = bookRepository.findById(new BookId(query.bookId()))
                .orElseThrow(() -> new BookNotFoundException("Book with bookId %d not found!".formatted(query.bookId())));
        Author author = authorRepository.findById(book.getAuthorId()).orElseThrow();
        return withPublicCover(bookDataMapper.toFindBookResponse(book, author));
    }

    @Override
    public PageResult<FindBookResponse> findAllBooks(PageQuery pageQuery) {
        PageResult<Book> booksPage = bookRepository.findAll(pageQuery);
        return new PageResult<>(
                booksPage.content().stream()
                        .map(book -> {
                            Author author = authorRepository.findById(book.getAuthorId()).orElseThrow();
                            return withPublicCover(bookDataMapper.toFindBookResponse(book, author));
                        })
                        .toList(),
                booksPage.page(),
                booksPage.size(),
                booksPage.totalElements(),
                booksPage.totalPages(),
                booksPage.first(),
                booksPage.last()
        );
    }

    private FindBookResponse withPublicCover(FindBookResponse raw) {
        return new FindBookResponse.Builder()
                .authorId(raw.getAuthorId())
                .authorName(raw.getAuthorName())
                .authorSurname(raw.getAuthorSurname())
                .bookId(raw.getBookId())
                .title(raw.getTitle())
                .about(raw.getAbout())
                .year(raw.getYear())
                .numberOfPages(raw.getNumberOfPages())
                .coverPath(fileManagementService.toPublicCoverUrl(raw.getCoverPath()))
                .build();
    }
}
