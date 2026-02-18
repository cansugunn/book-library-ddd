package com.finalproject.application.services;

import com.finalproject.application.dto.*;
import com.finalproject.application.mapper.UserBookStateMapper;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import com.finalproject.application.ports.output.fms.FileStoragePort;
import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.application.ports.output.repository.UserBookStateRepository;
import com.finalproject.application.ports.output.repository.UserRepository;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.domain.entity.*;
import com.finalproject.domain.exception.AuthorNotFoundException;
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
    private final FileStoragePort fileStoragePort;

    public UserBookStateApplicationServiceImpl(UserBookStateRepository userBookStateRepository,
                                               BookRepository bookRepository,
                                               AuthorRepository authorRepository,
                                               UserRepository userRepository,
                                               UserBookStateMapper userBookStateMapper,
                                               CurrentUser currentUser,
                                               FileStoragePort fileStoragePort) {
        this.userBookStateMapper = userBookStateMapper;
        this.userBookStateRepository = userBookStateRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
        this.currentUser = currentUser;
        this.fileStoragePort = fileStoragePort;
    }

    @Override
    public FindUserBookStateResponse findUserBookOfCurrentUser(int bookId) {
        BookId bookIdObjectValue = new BookId(bookId);
        Book book = bookRepository.findById(bookIdObjectValue)
                .orElseThrow(() -> new BookNotFoundException("Book with bookId %d not found!".formatted(bookId)));

        Author author = authorRepository.findById(book.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author for book %d not found!".formatted(bookId)));

        UserId userIdObjectValue = new UserId(currentUser.getId());
        Optional<UserBookState> userBookStateOptional =
                userBookStateRepository.findByBookIdAndUserId(bookIdObjectValue, userIdObjectValue);

        List<Comment> commentList = userBookStateOptional.map(UserBookState::getComments).orElse(Collections.emptyList());

        return withPublicCover(userBookStateMapper.toFindUserBookStateResponse(
                userBookStateOptional, book, author, commentList));
    }

    @Override
    public List<FindUserBookStateResponse> findFavouriteBooksOfCurrentUser() {
        return userBookStateRepository.findRatedOver(new UserId(currentUser.getId()), new Rating(3)).stream()
                .map(this::mapStateToResponse)
                .flatMap(Optional::stream)
                .map(this::withPublicCover)
                .toList();
    }

    @Override
    public List<FindUserBookStateResponse> findNotReadBooksYetOfCurrentUser() {
        UserId userIdValueObject = new UserId(currentUser.getId());
        List<FindUserBookStateResponse> result = new LinkedList<>();

        List<FindUserBookStateResponse> subResult =
                bookRepository.findBooksWithoutUserBookStateRecords(userIdValueObject)
                        .stream()
                        .map(book -> authorRepository.findById(book.getAuthorId())
                                .map(author -> userBookStateMapper.toFindUserBookStateResponse(
                                        Optional.empty(),
                                        book,
                                        author,
                                        Collections.emptyList())))
                        .flatMap(Optional::stream)
                        .toList();
        result.addAll(subResult);

        subResult = userBookStateRepository.findNotReadYetOf(userIdValueObject).stream()
                .map(this::mapStateToResponse)
                .flatMap(Optional::stream)
                .toList();
        result.addAll(subResult);

        return result.stream().map(this::withPublicCover).toList();
    }

    @Override
    public List<FindUserBookStateResponse> findWishedBooksToReadThatWillBeDoneIn1WeekOfCurrentUser() {
        if (!UserType.ADMIN.equals(currentUser.getUsertype())) {
            throw new UserDomainException("You not have permission to this operation!");
        }

        return userBookStateRepository.findToBeReadIn1Week(new UserId(currentUser.getId())).stream()
                .map(this::mapStateToResponse)
                .flatMap(Optional::stream)
                .map(this::withPublicCover)
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

    private Optional<FindUserBookStateResponse> mapStateToResponse(UserBookState userBookState) {
        return bookRepository.findById(userBookState.getBookId())
                .flatMap(book -> authorRepository.findById(book.getAuthorId())
                        .map(author -> userBookStateMapper.toFindUserBookStateResponse(
                                Optional.of(userBookState),
                                book,
                                author,
                                userBookState.getComments())));
    }

    private FindUserBookStateResponse withPublicCover(FindUserBookStateResponse raw) {
        return new FindUserBookStateResponse.Builder()
                .userBookStateId(raw.getUserBookStateId())
                .authorId(raw.getAuthorId())
                .authorName(raw.getAuthorName())
                .authorSurname(raw.getAuthorSurname())
                .bookId(raw.getBookId())
                .title(raw.getTitle())
                .about(raw.getAbout())
                .year(raw.getYear())
                .numberOfPages(raw.getNumberOfPages())
                .coverPath(fileStoragePort.resolvePublicUrl(raw.getCoverPath()))
                .read(raw.getRead())
                .rating(raw.getRating())
                .comments(raw.getComments())
                .releaseDate(raw.getReleaseDate())
                .build();
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
