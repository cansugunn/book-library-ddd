package com.finalproject.application.mapper;

import com.finalproject.application.dto.*;
import com.finalproject.domain.entity.*;
import com.finalproject.domain.valueobject.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserBookStateMapper {
    public FindUserBookStateResponse toFindUserBookStateResponse(Optional<UserBookState> userBookStateOptional,
                                                                 Book book,
                                                                 Author author,
                                                                 List<Comment> commentList) {
        return new FindUserBookStateResponse.Builder()
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
                .userBookStateId(userBookStateOptional
                        .map(UserBookState::getId)
                        .map(UserBookStateId::getValue)
                        .orElse(null))
                .read(userBookStateOptional
                        .map(UserBookState::getRead)
                        .orElse(Read.NOT_READ))
                .rating(userBookStateOptional
                        .map(UserBookState::getRating)
                        .map(Rating::getValue)
                        .orElse(0))
                .releaseDate(userBookStateOptional
                        .map(UserBookState::getReleaseDate)
                        .map(ReleaseDate::getDate)
                        .orElse(null))
                .comments(commentList.stream()
                        .map(Comment::getValue)
                        .toList())
                .build();
    }

    public UserBookState toUserBookState(UpdateUserBookStateRequest request,
                                         UserBookState userBookState) {
        return new UserBookState.Builder()
                .id(userBookState.getId())
                .userId(userBookState.getUserId())
                .bookId(userBookState.getBookId())
                .read(request.getRead())
                .rating(new Rating(request.getRating()))
                .releaseDate(Optional.ofNullable(request.getReleaseDate())
                        .map(ReleaseDate::new)
                        .orElse(null))
                .comments(request.getComments()
                        .stream()
                        .map(commentValue -> new Comment.Builder()
                                .value(commentValue)
                                .build())
                        .toList())
                .build();
    }

    public UserBookState toUserBookState(CreateUserBookStateRequest request,
                                         User user,
                                         Book book) {
        return new UserBookState.Builder()
                .userId(new UserId(user.getId().getValue()))
                .bookId(new BookId(book.getId().getValue()))
                .read(request.getRead())
                .rating(new Rating(request.getRating()))
                .releaseDate(Optional.ofNullable(request.getReleaseDate())
                        .map(ReleaseDate::new)
                        .orElse(null))
                .comments(request.getComments()
                        .stream()
                        .map(commentValue -> new Comment.Builder()
                                .value(commentValue)
                                .build())
                        .toList())
                .build();
    }

    public CreateUserBookStateResponse toCreateUserBookStateResponse(UserBookState userBookState) {
        return new CreateUserBookStateResponse.Builder()
                .id(userBookState.getId().getValue())
                .userId(userBookState.getUserId().getValue())
                .bookId(userBookState.getBookId().getValue())
                .read(userBookState.getRead())
                .rating(userBookState.getRating().getValue())
                .releaseDate(Optional.ofNullable(userBookState.getReleaseDate())
                        .map(ReleaseDate::getDate)
                        .orElse(null))
                .comments(userBookState.getComments()
                        .stream()
                        .map(Comment::getValue)
                        .toList())
                .build();
    }

    public UpdateUserBookStateResponse toUpdateUserBookStateResponse(UserBookState userBookState) {
        return new UpdateUserBookStateResponse.Builder()
                .id(userBookState.getId().getValue())
                .read(userBookState.getRead())
                .rating(userBookState.getRating().getValue())
                .releaseDate(Optional.ofNullable(userBookState.getReleaseDate())
                        .map(ReleaseDate::getDate)
                        .orElse(null))
                .comments(userBookState.getComments()
                        .stream()
                        .map(Comment::getValue)
                        .toList())
                .build();
    }
}
