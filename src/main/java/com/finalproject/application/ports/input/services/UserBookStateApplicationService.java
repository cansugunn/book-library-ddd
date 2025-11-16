package com.finalproject.application.ports.input.services;

import com.finalproject.application.dto.*;

import java.util.List;

public interface UserBookStateApplicationService {
    FindUserBookStateResponse findUserBookOfCurrentUser(int bookId);

    List<FindUserBookStateResponse> findFavouriteBooksOfCurrentUser();

    List<FindUserBookStateResponse> findNotReadBooksYetOfCurrentUser();

    List<FindUserBookStateResponse> findWishedBooksToReadThatWillBeDoneIn1WeekOfCurrentUser();

    CreateUserBookStateResponse createUserBookForCurrentUser(CreateUserBookStateRequest request);
    UpdateUserBookStateResponse updateUserBookForCurrentUser(UpdateUserBookStateRequest request);
}
