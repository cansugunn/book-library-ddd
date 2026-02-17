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

        entity.setId(domain.getId().getValue());
        entity.setUser(entityManager.getReference(UserJpaEntity.class, domain.getUserId().getValue()));
        entity.setBook(entityManager.getReference(BookJpaEntity.class, domain.getBookId().getValue()));
        entity.setReadStatus(domain.getRead().getOrdinaryValue());
        entity.setRating(domain.getRating().getValue());
        entity.setReleaseDate(
                Optional.ofNullable(domain.getReleaseDate())
                        .map(ReleaseDate::getDate)
                        .orElse(null));

        List<CommentJpaEntity> comments = domain.getComments()
                .stream()
                .map(commentJpaMapper::toEntity)
                .toList();
        entity.setComments(comments);

        return entity;
    }

    public UserBookState toDomain(UserBookStateJpaEntity entity) {
        List<Comment> comments = entity.getComments()
                .stream()
                .map(commentJpaMapper::toDomain)
                .toList();

        return new UserBookState.Builder()
                .id(new UserBookStateId(entity.getId()))
                .userId(new UserId(entity.getUser().getId()))
                .bookId(new BookId(entity.getBook().getId()))
                .read(Read.of(entity.getReadStatus()))
                .rating(new Rating(entity.getRating()))
                .releaseDate(Optional.ofNullable(entity.getReleaseDate())
                        .map(ReleaseDate::new)
                        .orElse(null))
                .comments(comments)
                .build();
    }
}
