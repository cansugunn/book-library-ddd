package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.Comment;
import com.finalproject.domain.entity.UserBookState;
import com.finalproject.domain.valueobject.CommentId;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.CommentJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.UserBookStateJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.CommentJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentJpaMapper {
    private final CommentJpaRepository commentJpaRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    public Comment toDomain(CommentJpaEntity entity) {
        return new Comment(new CommentId(entity.getId()), entity.getValue());
    }

    public CommentJpaEntity toEntity(Comment comment, UserBookStateJpaEntity userBookStateJpaEntity) {
        CommentJpaEntity entity = Optional.ofNullable(comment.getId())
                .map(CommentId::getValue)
                .flatMap(commentJpaRepository::findById)
                .orElseGet(CommentJpaEntity::new);

        entity.setId(Optional.ofNullable(comment.getId())
                .map(CommentId::getValue)
                .orElse(null));
        entity.setValue(comment.getValue());
        entity.setUserBookState(userBookStateJpaEntity);

        return entity;
    }
}
