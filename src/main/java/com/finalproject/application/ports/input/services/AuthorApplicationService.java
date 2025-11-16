package com.finalproject.application.ports.input.services;

import com.finalproject.application.dto.FindAuthorResponse;

import java.util.List;

public interface AuthorApplicationService {
    List<FindAuthorResponse> findAuthor(String name);

    List<FindAuthorResponse> displayFavouriteAuthorsOfCurrentUser();
}
