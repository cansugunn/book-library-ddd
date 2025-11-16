package com.finalproject.application.services;

import com.finalproject.application.dto.*;
import com.finalproject.application.mapper.BookDataMapper;
import com.finalproject.application.ports.input.services.BookApplicationService;
import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.application.ports.output.repository.UnitOfWork;
import com.finalproject.application.ports.output.repository.UserBookStateRepository;
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

public class BookApplicationServiceImpl implements BookApplicationService {
    private final BookDataMapper bookDataMapper;
    private final BookRepository bookRepository;
    private final UserBookStateRepository userBookStateRepository;
    private final AuthorRepository authorRepository;
    private final UnitOfWork unitOfWork;
    private final CurrentUser currentUser;

    public BookApplicationServiceImpl(BookRepository bookRepository,
                                      UserBookStateRepository userBookStateRepository,
                                      AuthorRepository authorRepository,
                                      UnitOfWork unitOfWork,
                                      BookDataMapper bookDataMapper,
                                      CurrentUser currentUser) {
        this.bookDataMapper = bookDataMapper;
        this.bookRepository = bookRepository;
        this.userBookStateRepository = userBookStateRepository;
        this.authorRepository = authorRepository;
        this.unitOfWork = unitOfWork;
        this.currentUser = currentUser;
    }

    //todo create records as no readed for all
    @Override
    public CreateBookResponse createBook(CreateBookRequest request) {
        if(!UserType.ADMIN.equals(currentUser.getUsertype())){
            throw new UserDomainException("You not have permission to this operation!");
        }

        return unitOfWork.executeInTransaction(() -> {
            Author author;
            if (!authorRepository.existsByNameAndSurname(request.getAuthorName(), request.getAuthorSurname())) {
                author = authorRepository.save(new Author.Builder()
                        .name(request.getAuthorName())
                        .surname(request.getAuthorSurname())
                        .build());
            } else {
                author = authorRepository
                        .findByNameAndSurname(request.getAuthorName(), request.getAuthorSurname())
                        .orElse(null);
            }
            Book book = bookDataMapper.toBook(request, author);
            book.validate();
            return bookDataMapper.toCreateBookResponse(bookRepository.save(book), author);
        });
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookRequest request) {
        if(!UserType.ADMIN.equals(currentUser.getUsertype())){
            throw new UserDomainException("You not have permission to this operation!");
        }

        BookId bookId = new BookId(request.getBookId());
        Book oldBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with bookId %d not found!"
                        .formatted(request.getBookId())));
        AuthorId oldAuthorId = oldBook.getAuthorId();

        return unitOfWork.executeInTransaction(() -> {
            Author author;
            if (!authorRepository.existsByNameAndSurname(request.getAuthorName(), request.getAuthorSurname())) {
                author = authorRepository.save(new Author.Builder()
                        .name(request.getAuthorName())
                        .surname(request.getAuthorSurname())
                        .build());
            } else {
                author = authorRepository
                        .findByNameAndSurname(request.getAuthorName(), request.getAuthorSurname())
                        .orElse(null);
            }

            Book newBook = bookDataMapper.toBook(request, author);
            newBook.validate();
            UpdateBookResponse updateBookResponse =
                    bookDataMapper.toUpdateBookResponse(bookRepository.update(newBook), author);

            if(!author.getId().equals(oldAuthorId) &&
                    !authorRepository.hasMoreBooksExcluding(oldAuthorId, bookId)) {
                authorRepository.deleteById(oldAuthorId);
            }

            return updateBookResponse;
        });
    }

    @Override
    public void deleteBook(int id) {
        if(!UserType.ADMIN.equals(currentUser.getUsertype())){
            throw new UserDomainException("You not have permission to this operation!");
        }

        if (!bookRepository.exists(new BookId(id))) {
            throw new BookNotFoundException("Book with bookId %d not found!".formatted(id));
        }

        Optional<Author> authorOptional = authorRepository.findByBookId(new BookId(id));
        if (authorOptional.isEmpty()) {
            throw new AuthorNotFoundException("author of book with bookId %d not found!".formatted(id));
        }

        unitOfWork.executeInTransaction(() -> {
            bookRepository.delete(new BookId(id));

            if (!authorRepository.hasMoreBooks(authorOptional.get().getId())) {
                authorRepository.deleteById(authorOptional.get().getId());
            }

            return null;
        });
    }

    @Override
    public FindBookResponse findBook(int bookId) {
        BookId bookIdValueObject = new BookId(bookId);
        Optional<Book> bookOptional = bookRepository.findById(bookIdValueObject);
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("Book with bookId %d not found!".formatted(bookId));
        }
        Optional<Author> authorOptional = authorRepository.findById(bookOptional.get().getAuthorId());
        return bookDataMapper.toFindBookResponse(bookOptional.get(), authorOptional.get());
    }
}
