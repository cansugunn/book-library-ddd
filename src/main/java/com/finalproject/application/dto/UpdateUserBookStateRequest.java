package com.finalproject.application.dto;

import com.finalproject.domain.valueobject.Read;

import java.util.Date;
import java.util.List;

public class UpdateUserBookStateRequest {
    private final int id;
    private final Read read;
    private final Integer rating;
    private final List<String> comments;
    private final Date releaseDate;

    private UpdateUserBookStateRequest(Builder builder) {
        id = builder.id;
        read = builder.read;
        rating = builder.rating;
        comments = builder.comments;
        releaseDate = builder.releaseDate;
    }


    public static final class Builder {
        private int id;
        private Read read;
        private Integer rating;
        private List<String> comments;
        private Date releaseDate;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
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

        public UpdateUserBookStateRequest build() {
            return new UpdateUserBookStateRequest(this);
        }
    }

    public int getId() {
        return id;
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
