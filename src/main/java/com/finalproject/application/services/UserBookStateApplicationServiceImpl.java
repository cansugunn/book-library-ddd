package com.finalproject.application.services;

import com.finalproject.application.dto.*;
import com.finalproject.application.mapper.UserBookStateMapper;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.application.ports.output.repository.UserBookStateRepository;
import com.finalproject.application.ports.output.repository.UserRepository;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.domain.entity.*;
import com.finalproject.domain.exception.BookNotFoundException;
import com.finalproject.domain.exception.UserBookStateNotFoundException;
import com.finalproject.domain.exception.UserDomainException;
import com.finalproject.domain.exception.UserNotFoundException;
import com.finalproject.domain.valueobject.*;

import java.util.*;

//todo projection
public class UserBookStateApplicationServiceImpl implements UserBookStateApplicationService {
    private final UserBookStateMapper userBookStateMapper;
    private final UserBookStateRepository userBookStateRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    public UserBookStateApplicationServiceImpl(UserBookStateRepository userBookStateRepository,
                                               BookRepository bookRepository,
                                               AuthorRepository authorRepository,
                                               UserRepository userRepository,
                                               UserBookStateMapper userBookStateMapper,
                                               CurrentUser currentUser) {
        this.userBookStateMapper = userBookStateMapper;
        this.userBookStateRepository = userBookStateRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    @Override
    public FindUserBookStateResponse findUserBookOfCurrentUser(int bookId) {
        BookId bookIdObjectValue = new BookId(bookId);
        Optional<Book> bookOptional = bookRepository.findById(bookIdObjectValue);
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("Book with bookId %d not found!".formatted(bookId));
        }
        Optional<Author> authorOptional = authorRepository.findById(bookOptional.get().getAuthorId());
        UserId userIdObjectValue = new UserId(currentUser.getId());
        Optional<UserBookState> userBookStateOptional =
                userBookStateRepository.findByBookIdAndUserId(bookIdObjectValue, userIdObjectValue);
        List<Comment> commentList = Collections.emptyList();
        if (userBookStateOptional.isPresent()) {
            commentList = userBookStateOptional.get().getComments();
        }
        return userBookStateMapper.toFindUserBookStateResponse(
                userBookStateOptional, bookOptional.get(), authorOptional.get(), commentList);
    }

    @Override
    public List<FindUserBookStateResponse> findFavouriteBooksOfCurrentUser() {
        return userBookStateRepository.findRatedOver(new UserId(currentUser.getId()), new Rating(3)).stream()
                .map(userBookState -> {
                    Book book = bookRepository.findById(userBookState.getBookId()).orElse(null);
                    Author author = authorRepository.findById(book.getAuthorId()).orElse(null);
                    List<Comment> commentList = userBookState.getComments();
                    return userBookStateMapper.toFindUserBookStateResponse(Optional.of(userBookState), book, author, commentList);
                })
                .toList();
    }

    @Override
    public List<FindUserBookStateResponse> findNotReadBooksYetOfCurrentUser() {
        UserId userIdValueObject = new UserId(currentUser.getId());
        List<FindUserBookStateResponse> result = new LinkedList<>();

        List<FindUserBookStateResponse> subResult =
                bookRepository.findBooksWithoutUserBookStateRecords(userIdValueObject)
                        .stream()
                        .map(book -> {
                            Author author = authorRepository.findById(book.getAuthorId()).orElse(null);
                            return userBookStateMapper.toFindUserBookStateResponse(
                                    Optional.empty(),
                                    book,
                                    author,
                                    Collections.emptyList());

                        }).toList();
        result.addAll(subResult);


        subResult = userBookStateRepository.findNotReadYetOf(userIdValueObject).stream()
                .map(userBookState -> {
                    Book book = bookRepository.findById(userBookState.getBookId()).orElse(null);
                    Author author = authorRepository.findById(book.getAuthorId()).orElse(null);
                    List<Comment> commentList = userBookState.getComments();
                    return userBookStateMapper.toFindUserBookStateResponse(Optional.of(userBookState), book, author, commentList);
                })
                .toList();
        result.addAll(subResult);

        return result;
    }

    @Override
    public List<FindUserBookStateResponse> findWishedBooksToReadThatWillBeDoneIn1WeekOfCurrentUser() {
        if (!UserType.ADMIN.equals(currentUser.getUsertype())) {
            throw new UserDomainException("You not have permission to this operation!");
        }

        return userBookStateRepository.findToBeReadIn1Week(new UserId(currentUser.getId())).stream()
                .map(userBookState -> {
                    Book book = bookRepository.findById(userBookState.getBookId()).orElse(null);
                    Author author = authorRepository.findById(book.getAuthorId()).orElse(null);
                    List<Comment> commentList = userBookState.getComments();
                    return userBookStateMapper.toFindUserBookStateResponse(Optional.of(userBookState), book, author, commentList);
                })
                .toList();
    }

    @Override
    public CreateUserBookStateResponse createUserBookForCurrentUser(CreateUserBookStateRequest request) {
        Book book = bookRepository.findById(new BookId(request.getBookId()))
                .orElseThrow(() -> new BookNotFoundException("Book with bookId %d not found!"
                        .formatted(request.getBookId())));
        User user = userRepository.findById(new UserId(currentUser.getId()))
                .orElseThrow(() -> new UserNotFoundException("User with userId %d not found!"));

        UserBookState userBookState = userBookStateMapper.toUserBookState(request, user, book);
        userBookState.validate();
        userBookState = userBookStateRepository.save(userBookState);
        return userBookStateMapper.toCreateUserBookStateResponse(userBookState);
    }

    @Override
    public UpdateUserBookStateResponse updateUserBookForCurrentUser(UpdateUserBookStateRequest request) {
        UserBookState userBookState = userBookStateRepository.findById(new UserBookStateId(request.getId()))
                .orElseThrow(() -> new UserBookStateNotFoundException("User Book state with id %d not found!"
                        .formatted(request.getId())));

        if (!Objects.equals(userBookState.getUserId().getValue(), currentUser.getId())) {
            throw new UserDomainException("You not have permission to this operation!");
        }

        userBookState = userBookStateMapper.toUserBookState(request, userBookState);
        userBookState = userBookStateRepository.update(userBookState);
        return userBookStateMapper.toUpdateUserBookStateResponse(userBookState);
    }
}
