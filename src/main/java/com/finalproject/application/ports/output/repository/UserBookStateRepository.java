package com.finalproject.application.ports.output.repository;

import com.finalproject.domain.entity.UserBookState;
import com.finalproject.domain.valueobject.BookId;
import com.finalproject.domain.valueobject.Rating;
import com.finalproject.domain.valueobject.UserBookStateId;
import com.finalproject.domain.valueobject.UserId;

import java.util.List;
import java.util.Optional;

public interface UserBookStateRepository {

    Optional<UserBookState> findByBookIdAndUserId(BookId bookId, UserId userId);

    List<UserBookState> findRatedOver(UserId userId, Rating rating);

    List<UserBookState> findNotReadYetOf(UserId userId);

    List<UserBookState> findToBeReadIn1Week(UserId userId);

    UserBookState save(UserBookState userBookState);

    UserBookState update(UserBookState userBookState);

    void deleteByBookId(BookId bookId);

    boolean exists(UserBookStateId userBookStateId);

    Optional<UserBookState> findById(UserBookStateId userBookStateId);
}
