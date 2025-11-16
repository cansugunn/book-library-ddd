package com.finalproject.application.mapper;

import com.finalproject.application.dto.FindAuthorResponse;
import com.finalproject.domain.entity.Author;
import com.finalproject.domain.valueobject.AuthorId;
import com.finalproject.domain.valueobject.Website;

import java.util.Optional;

public class AuthorDataMapper {
    public FindAuthorResponse toFindAuthorResponse(Author author) {
        return new FindAuthorResponse.Builder()
                .id(Optional.ofNullable(author.getId())
                        .map(AuthorId::getValue)
                        .orElse(null))
                .name(author.getName())
                .surname(author.getSurname())
                .websiteUrl(Optional.ofNullable(author.getWebsite())
                        .map(Website::getUrl)
                        .orElse(null))
                .build();
    }
}
