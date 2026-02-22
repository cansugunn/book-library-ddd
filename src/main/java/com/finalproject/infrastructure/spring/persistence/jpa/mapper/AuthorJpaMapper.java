package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.Author;
import com.finalproject.domain.valueobject.AuthorId;
import com.finalproject.domain.valueobject.Website;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.AuthorJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.AuthorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorJpaMapper {
    private final AuthorJpaRepository authorJpaRepository;

    public AuthorJpaEntity toEntity(Author author) {
        AuthorJpaEntity entity = Optional.ofNullable(author.getId())
                .map(AuthorId::getValue)
                .flatMap(authorJpaRepository::findById)
                .orElseGet(AuthorJpaEntity::new);

        entity.setId(
                Optional.ofNullable(author.getId())
                        .map(AuthorId::getValue)
                        .orElse(null));
        entity.setName(author.getName());
        entity.setSurname(author.getSurname());
        entity.setWebsite(
                Optional.ofNullable(author.getWebsite())
                        .map(Website::getUrl)
                        .orElse(null));
        return entity;
    }

    public Author toDomain(AuthorJpaEntity entity) {
        return new Author.Builder()
                .id(Optional.ofNullable(entity.getId())
                        .map(AuthorId::new)
                        .orElse(null))
                .name(entity.getName())
                .surname(entity.getSurname())
                .website(Optional.ofNullable(entity.getWebsite())
                        .map(Website::new)
                        .orElse(null))
                .build();
    }
}
