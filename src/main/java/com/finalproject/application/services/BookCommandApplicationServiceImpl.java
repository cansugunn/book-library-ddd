package com.finalproject.application.services;

import com.finalproject.application.dto.CreateBookResponse;
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
        if (!UserType.ADMIN.equals(currentUser.getUsertype())) {
            throw new UserDomainException("You not have permission to this operation!");
        }

        return unitOfWork.executeInTransaction(() -> {
            Author author;
            if (!authorRepository.existsByNameAndSurname(command.authorName(), command.authorSurname())) {
                author = authorRepository.save(new Author.Builder()
                        .name(command.authorName())
                        .surname(command.authorSurname())
                        .build());
            } else {
                author = authorRepository
                        .findByNameAndSurname(command.authorName(), command.authorSurname())
                        .orElse(null);
            }
            Book book = bookDataMapper.toBook(command, author);
            book.validate();
            return bookDataMapper.toCreateBookResponse(bookRepository.save(book), author);
        });
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        if (!currentUser.isAdmin()) {
            throw new UserDomainException("You not have permission to this operation!");
        }

        BookId bookId = new BookId(command.bookId());
        Book oldBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with bookId %d not found!"
                        .formatted(command.bookId())));
        AuthorId oldAuthorId = oldBook.getAuthorId();

        return unitOfWork.executeInTransaction(() -> {
            Author author;
            if (!authorRepository.existsByNameAndSurname(command.authorName(), command.authorSurname())) {
                author = authorRepository.save(new Author.Builder()
                        .name(command.authorName())
                        .surname(command.authorSurname())
                        .build());
            } else {
                author = authorRepository
                        .findByNameAndSurname(command.authorName(), command.authorSurname())
                        .orElse(null);
            }

            Book newBook = bookDataMapper.toBook(command, author);
            newBook.validate();
            UpdateBookResponse updateBookResponse =
                    bookDataMapper.toUpdateBookResponse(bookRepository.update(newBook), author);

            if (!author.getId().equals(oldAuthorId) &&
                    !authorRepository.hasMoreBooksExcluding(oldAuthorId, bookId)) {
                authorRepository.deleteById(oldAuthorId);
            }

            return updateBookResponse;
        });
    }

    @Override
    public void deleteBook(DeleteBookCommand command) {
        if (!currentUser.isAdmin()) {
            throw new UserDomainException("You not have permission to this operation!");
        }

        BookId bookId = new BookId(command.bookId());
        if (!bookRepository.exists(bookId)) {
            throw new BookNotFoundException("Book with bookId %d not found!".formatted(bookId.getValue()));
        }

        Author author = authorRepository.findByBookId(bookId).orElseThrow(() ->
                new AuthorNotFoundException("author of book with bookId %d not found!".formatted(bookId.getValue())));

        unitOfWork.executeInTransaction(() -> {
            bookRepository.delete(bookId);
            if (!authorRepository.hasMoreBooks(author.getId())) {
                authorRepository.deleteById(author.getId());
            }
            return null;
        });
    }
}
