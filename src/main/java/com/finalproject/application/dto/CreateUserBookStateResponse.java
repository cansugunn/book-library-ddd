package com.finalproject.application.dto;

import com.finalproject.domain.valueobject.Read;

import java.util.Date;
import java.util.List;

public class CreateUserBookStateResponse {
    private final Integer id;
    private final Integer userId;
    private final Integer bookId;
    private final Read read;
    private final Integer rating;
    private final List<String> comments;
    private final Date releaseDate;

    private CreateUserBookStateResponse(Builder builder) {
        id = builder.id;
        userId = builder.userId;
        bookId = builder.bookId;
        read = builder.read;
        rating = builder.rating;
        comments = builder.comments;
        releaseDate = builder.releaseDate;
    }


    public static final class Builder {
        private Integer id;
        private Integer userId;
        private Integer bookId;
        private Read read;
        private Integer rating;
        private List<String> comments;
        private Date releaseDate;

        public Builder() {
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Builder userId(Integer val) {
            userId = val;
            return this;
        }

        public Builder bookId(Integer val) {
            bookId = val;
            return this;
        }

        public Builder read(Read val) {
            read = val;
            return this;
        }

        public Builder rating(Integer val) {
            rating = val;
            return this;
        }

        public Builder comments(List<String> val) {
            comments = val;
            return this;
        }

        public Builder releaseDate(Date val) {
            releaseDate = val;
            return this;
        }

        public CreateUserBookStateResponse build() {
            return new CreateUserBookStateResponse(this);
        }
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public Read getRead() {
        return read;
    }

    public Integer getRating() {
        return rating;
    }

    public List<String> getComments() {
        return comments;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
}
