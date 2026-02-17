package com.finalproject.application.services;

import com.finalproject.application.dto.CreateBookRequest;
import com.finalproject.application.dto.CreateBookResponse;
import com.finalproject.application.dto.UpdateBookRequest;
import com.finalproject.application.dto.UpdateBookResponse;
import com.finalproject.application.dto.book.command.CreateBookCommand;
import com.finalproject.application.dto.book.command.DeleteBookCommand;
import com.finalproject.application.dto.book.command.UpdateBookCommand;
import com.finalproject.application.mapper.BookDataMapper;
import com.finalproject.application.ports.input.services.BookCommandApplicationService;
import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.application.ports.output.repository.UnitOfWork;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.domain.entity.Author;
import com.finalproject.domain.entity.Book;
import com.finalproject.domain.exception.AuthorNotFoundException;
import com.finalproject.domain.exception.BookNotFoundException;
import com.finalproject.domain.exception.UserDomainException;
import com.finalproject.domain.valueobject.AuthorId;
import com.finalproject.domain.valueobject.BookId;
import com.finalproject.domain.valueobject.UserType;

import java.util.Optional;

public class BookCommandApplicationServiceImpl implements BookCommandApplicationService {
    private final BookDataMapper bookDataMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UnitOfWork unitOfWork;
    private final CurrentUser currentUser;

    public BookCommandApplicationServiceImpl(BookRepository bookRepository,
                                             AuthorRepository authorRepository,
                                             UnitOfWork unitOfWork,
                                             BookDataMapper bookDataMapper,
                                             CurrentUser currentUser) {
        this.bookDataMapper = bookDataMapper;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.unitOfWork = unitOfWork;
        this.currentUser = currentUser;
    }

    @Override
    public CreateBookResponse createBook(CreateBookCommand command) {
        assertAdmin();

        return unitOfWork.executeInTransaction(() -> {
            Author author = findOrCreateAuthor(command.authorName(), command.authorSurname());
            CreateBookRequest request = new CreateBookRequest.Builder()
                    .authorName(command.authorName())
                    .authorSurname(command.authorSurname())
                    .title(command.title())
                    .year(command.year())
                    .numberOfPages(command.numberOfPages())
                    .about(command.about())
                    .coverPath(command.coverPath())
                    .build();

            Book book = bookDataMapper.toBook(request, author);
            book.validate();
            return bookDataMapper.toCreateBookResponse(bookRepository.save(book), author);
        });
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        assertAdmin();

        BookId bookId = new BookId(command.bookId());
        Book oldBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with bookId %d not found!".formatted(command.bookId())));
        AuthorId oldAuthorId = oldBook.getAuthorId();

        return unitOfWork.executeInTransaction(() -> {
            Author author = findOrCreateAuthor(command.authorName(), command.authorSurname());
            UpdateBookRequest request = new UpdateBookRequest.Builder()
                    .bookId(command.bookId())
                    .authorName(command.authorName())
                    .authorSurname(command.authorSurname())
                    .title(command.title())
                    .year(command.year())
                    .numberOfPages(command.numberOfPages())
                    .about(command.about())
                    .coverPath(command.coverPath())
                    .build();

            Book newBook = bookDataMapper.toBook(request, author);
            newBook.validate();
            UpdateBookResponse response = bookDataMapper.toUpdateBookResponse(bookRepository.update(newBook), author);

            if (!author.getId().equals(oldAuthorId) && !authorRepository.hasMoreBooksExcluding(oldAuthorId, bookId)) {
                authorRepository.deleteById(oldAuthorId);
            }

            return response;
        });
    }

    @Override
    public void deleteBook(DeleteBookCommand command) {
        assertAdmin();

        BookId bookId = new BookId(command.bookId());
        if (!bookRepository.exists(bookId)) {
            throw new BookNotFoundException("Book with bookId %d not found!".formatted(command.bookId()));
        }

        Optional<Author> authorOptional = authorRepository.findByBookId(bookId);
        if (authorOptional.isEmpty()) {
            throw new AuthorNotFoundException("author of book with bookId %d not found!".formatted(command.bookId()));
        }

        unitOfWork.executeInTransaction(() -> {
            bookRepository.delete(bookId);
            if (!authorRepository.hasMoreBooks(authorOptional.get().getId())) {
                authorRepository.deleteById(authorOptional.get().getId());
            }
            return null;
        });
    }

    private void assertAdmin() {
        if (!UserType.ADMIN.equals(currentUser.getUsertype())) {
            throw new UserDomainException("You not have permission to this operation!");
        }
    }

    private Author findOrCreateAuthor(String name, String surname) {
        if (!authorRepository.existsByNameAndSurname(name, surname)) {
            return authorRepository.save(new Author.Builder().name(name).surname(surname).build());
        }
        return authorRepository.findByNameAndSurname(name, surname).orElseThrow();
    }
}
