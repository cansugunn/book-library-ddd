package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.Comment;
import com.finalproject.domain.entity.UserBookState;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.CommentJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.UserBookStateJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.UserJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.UserBookStateJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserBookStateJpaMapper {
    private final UserBookStateJpaRepository userBookStateJpaRepository;
    private final CommentJpaMapper commentJpaMapper;
    @PersistenceContext
    private final EntityManager entityManager;

    public UserBookStateJpaEntity toEntity(UserBookState domain) {
        UserBookStateJpaEntity entity = Optional.ofNullable(domain.getId())
                .map(UserBookStateId::getValue)
                .flatMap(userBookStateJpaRepository::findById)
                .orElseGet(UserBookStateJpaEntity::new);

        entity.setId(Optional.ofNullable(domain.getId())
                .map(UserBookStateId::getValue)
                .orElse(null));
        entity.setUser(Optional.ofNullable(domain.getUserId())
                .map(UserId::getValue)
                .map(i -> entityManager.getReference(UserJpaEntity.class, i))
                .orElse(null));
        entity.setBook(Optional.ofNullable(domain.getBookId())
                .map(BookId::getValue)
                .map(i -> entityManager.getReference(BookJpaEntity.class, i))
                .orElse(null));
        entity.setReadStatus(Optional.ofNullable(domain.getRead())
                .map(Read::getOrdinaryValue)
                .orElse(null));
        entity.setRating(Optional.ofNullable(domain.getRating())
                .map(Rating::getValue)
                .orElse(null));
        entity.setReleaseDate(
                Optional.ofNullable(domain.getReleaseDate())
                        .map(ReleaseDate::getDate)
                        .orElse(null));

        List<CommentJpaEntity> comments = domain.getComments()
                .stream()
                .map(comment -> commentJpaMapper.toEntity(comment, entity))
                .collect(Collectors.toList()); //mutable list for hibernate
        //for orphan removal
        entity.getComments().clear();
        entity.getComments().addAll(comments);

        return entity;
    }

    public UserBookState toDomain(UserBookStateJpaEntity entity) {
        List<Comment> comments = entity.getComments()
                .stream()
                .map(commentJpaMapper::toDomain)
                .toList();

        return new UserBookState.Builder()
                .id(Optional.ofNullable(entity.getId())
                        .map(UserBookStateId::new)
                        .orElse(null))
                .userId(Optional.ofNullable(entity.getUser())
                        .map(UserJpaEntity::getId)
                        .map(UserId::new)
                        .orElse(null))
                .bookId(Optional.ofNullable(entity.getBook())
                        .map(BookJpaEntity::getId)
                        .map(BookId::new)
                        .orElse(null))
                .read(Read.of(entity.getReadStatus()))
                .rating(Optional.ofNullable(entity.getRating())
                        .map(Rating::new)
                        .orElse(null))
                .releaseDate(Optional.ofNullable(entity.getReleaseDate())
                        .map(ReleaseDate::new)
                        .orElse(null))
                .comments(comments)
                .build();
    }
}
