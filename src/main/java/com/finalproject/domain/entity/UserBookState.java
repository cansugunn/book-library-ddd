package com.finalproject.domain.entity;

import com.finalproject.domain.exception.UserBookStateDomainException;
import com.finalproject.domain.valueobject.*;

import java.util.List;
import java.util.Objects;

public class UserBookState extends AggregateRoot<UserBookStateId> {
    private final UserId userId;
    private final BookId bookId;
    private final Read read;
    private final Rating rating;
    private final List<Comment> comments;
    private final ReleaseDate releaseDate;

    private UserBookState(Builder builder) {
        super(builder.id);
        userId = builder.userId;
        bookId = builder.bookId;
        read = builder.read;
        rating = builder.rating;
        comments = builder.comments;
        releaseDate = builder.releaseDate;
    }

    @Override
    public void validate() {
        validateRead();
        validateRating();
        validateReleaseDate();
    }

    private void validateRead() {
        if (read == null) {
            throw new UserBookStateDomainException("Read must not be null!");
        }
    }

    private void validateReleaseDate() {
        if (Objects.equals(read, Read.WISH_TO_BE_READ)) {
            if (releaseDate == null) {
                throw new UserBookStateDomainException("Invalid release date!");
            }
        } else {
            if (releaseDate != null) {
                throw new UserBookStateDomainException("Release date must be null!");
            }
        }
    }

    private void validateRating() {
        if (rating == null) {
            throw new UserBookStateDomainException("Rating must not be null!");
        }

        if (Objects.equals(read, Read.READ)) {
            if (rating.getValue() < 1 || rating.getValue() > 5) {
                throw new UserBookStateDomainException("Invalid rating!");
            }
        } else {
            if (rating.getValue() < 0 || rating.getValue() > 5) {
                throw new UserBookStateDomainException("Invalid rating!");
            }
        }

    }

    public UserId getUserId() {
        return userId;
    }

    public BookId getBookId() {
        return bookId;
    }

    public Read getRead() {
        return read;
    }

    public Rating getRating() {
        return rating;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public ReleaseDate getReleaseDate() {
        return releaseDate;
    }

    public static final class Builder {
        private UserBookStateId id;
        private UserId userId;
        private BookId bookId;
        private Read read;
        private Rating rating;
        private List<Comment> comments;
        private ReleaseDate releaseDate;

        public Builder() {
        }

        public Builder id(UserBookStateId val) {
            id = val;
            return this;
        }

        public Builder userId(UserId val) {
            userId = val;
            return this;
        }

        public Builder bookId(BookId val) {
            bookId = val;
            return this;
        }

        public Builder read(Read val) {
            read = val;
            return this;
        }

        public Builder rating(Rating val) {
            rating = val;
            return this;
        }

        public Builder comments(List<Comment> val) {
            comments = val;
            return this;
        }

        public Builder releaseDate(ReleaseDate val) {
            releaseDate = val;
            return this;
        }

        public UserBookState build() {
            return new UserBookState(this);
        }
    }
}
