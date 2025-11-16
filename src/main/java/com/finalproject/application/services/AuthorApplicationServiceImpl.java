package com.finalproject.application.services;

import com.finalproject.application.dto.FindAuthorResponse;
import com.finalproject.application.mapper.AuthorDataMapper;
import com.finalproject.application.ports.input.services.AuthorApplicationService;
import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.domain.valueobject.Rating;
import com.finalproject.domain.valueobject.UserId;

import java.util.List;

public class AuthorApplicationServiceImpl implements AuthorApplicationService {
    private final AuthorRepository authorRepository;
    private final AuthorDataMapper authorDataMapper;
    private final CurrentUser currentUser;

    public AuthorApplicationServiceImpl(AuthorRepository authorRepository,
                                        AuthorDataMapper authorDataMapper,
                                        CurrentUser currentUser) {
        this.authorRepository = authorRepository;
        this.authorDataMapper = authorDataMapper;
        this.currentUser = currentUser;
    }

    @Override
    public List<FindAuthorResponse> findAuthor(String name) {
        return authorRepository.findByName(name).stream()
                .map(authorDataMapper::toFindAuthorResponse)
                .toList();
    }

    @Override
    public List<FindAuthorResponse> displayFavouriteAuthorsOfCurrentUser() {
        return authorRepository
                .findWhichUserHasAtLeastThreeBooksRatedOver(new UserId(currentUser.getId()), new Rating(3))
                .stream()
                .map(authorDataMapper::toFindAuthorResponse)
                .toList();
    }
}
