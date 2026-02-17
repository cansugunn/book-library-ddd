package com.finalproject.infrastructure.persistence.jpa.adapter;

import com.finalproject.application.ports.output.repository.UserBookStateRepository;
import com.finalproject.domain.entity.Comment;
import com.finalproject.domain.entity.UserBookState;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.persistence.jpa.entity.CommentJpaEntity;
import com.finalproject.infrastructure.persistence.jpa.entity.UserBookStateJpaEntity;
import com.finalproject.infrastructure.persistence.jpa.repository.SpringDataUserBookStateRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JpaUserBookStateRepositoryAdapter implements UserBookStateRepository {
    private final SpringDataUserBookStateRepository repository;

    public JpaUserBookStateRepositoryAdapter(SpringDataUserBookStateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserBookState> findByBookIdAndUserId(BookId bookId, UserId userId) {
        return repository.findByBookIdAndUserId(bookId.getValue(), userId.getValue()).map(this::toDomain);
    }

    @Override
    public List<UserBookState> findRatedOver(UserId userId, Rating rating) {
        return repository.findByUserIdAndRatingGreaterThan(userId.getValue(), rating.getValue()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<UserBookState> findNotReadYetOf(UserId userId) {
        return repository.findByUserIdAndReadStatusNot(userId.getValue(), Read.READ.getOrdinaryValue()).stream().map(this::toDomain).toList();
    }

    @Override
    public List<UserBookState> findToBeReadIn1Week(UserId userId) {
        Date weekDate = new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000);
        return repository.findWishToReadInWeek(userId.getValue(), Read.WISH_TO_BE_READ.getOrdinaryValue(), weekDate)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public UserBookState save(UserBookState userBookState) {
        return toDomain(repository.save(toEntity(userBookState)));
    }

    @Override
    public UserBookState update(UserBookState userBookState) {
        return toDomain(repository.save(toEntity(userBookState)));
    }

    @Override
    public void deleteByBookId(BookId bookId) {
        repository.deleteByBookId(bookId.getValue());
    }

    @Override
    public boolean exists(UserBookStateId userBookStateId) {
        return repository.existsById(userBookStateId.getValue());
    }

    @Override
    public Optional<UserBookState> findById(UserBookStateId userBookStateId) {
        return repository.findById(userBookStateId.getValue()).map(this::toDomain);
    }

    private UserBookStateJpaEntity toEntity(UserBookState domain) {
        UserBookStateJpaEntity entity = new UserBookStateJpaEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }
        entity.setUserId(domain.getUserId().getValue());
        entity.setBookId(domain.getBookId().getValue());
        entity.setReadStatus(domain.getRead().getOrdinaryValue());
        entity.setRating(domain.getRating().getValue());
        entity.setReleaseDate(domain.getReleaseDate() == null ? null : domain.getReleaseDate().getDate());

        List<CommentJpaEntity> comments = domain.getComments().stream().map(comment -> {
            CommentJpaEntity ce = new CommentJpaEntity();
            if (comment.getId() != null) {
                ce.setId(comment.getId().getValue());
            }
            ce.setValue(comment.getValue());
            ce.setUserBookState(entity);
            return ce;
        }).toList();
        entity.setComments(comments);
        return entity;
    }

    private UserBookState toDomain(UserBookStateJpaEntity entity) {
        List<Comment> comments = entity.getComments().stream()
                .map(comment -> new Comment(new CommentId(comment.getId()), comment.getValue()))
                .toList();
        return new UserBookState.Builder()
                .id(new UserBookStateId(entity.getId()))
                .userId(new UserId(entity.getUserId()))
                .bookId(new BookId(entity.getBookId()))
                .read(Read.of(entity.getReadStatus()))
                .rating(new Rating(entity.getRating()))
                .releaseDate(entity.getReleaseDate() == null ? null : new ReleaseDate(entity.getReleaseDate()))
                .comments(comments)
                .build();
    }
}
