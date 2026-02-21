package com.finalproject.application.ports.input.services;

import com.finalproject.application.dto.*;
import com.finalproject.application.dto.book.query.SearchBooksQuery;
import com.finalproject.application.dto.page.PageResult;

import java.util.List;

public interface UserBookStateApplicationService {
//    PageResult<FindBookCommentResponse> findBookComments(SearchBookCommentsQuery query);

    FindUserBookStateResponse findUserBookOfCurrentUser(int bookId);

    FindUserBookStatisticsResponse findUserBookStatistics(int bookId);

    List<FindUserBookStateResponse> findFavouriteBooksOfCurrentUser();

    List<FindUserBookStateResponse> findNotReadBooksYetOfCurrentUser();

    List<FindUserBookStateResponse> findWishedBooksToReadThatWillBeDoneIn1WeekOfCurrentUser();

    CreateUserBookStateResponse createUserBookForCurrentUser(CreateUserBookStateRequest request);

    UpdateUserBookStateResponse updateUserBookForCurrentUser(UpdateUserBookStateRequest request);
}
