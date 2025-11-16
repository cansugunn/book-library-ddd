package com.finalproject.application.dto;

import com.finalproject.domain.valueobject.Read;

import java.util.Date;
import java.util.List;

public class FindUserBookStateResponse {
    private final Integer userBookStateId;
    private final Integer authorId;
    private final String authorName;
    private final String authorSurname;
    private final Integer bookId;
    private final String title;
    private final String about;
    private final Integer year;
    private final Integer numberOfPages;
    private final String coverPath;
    private final Read read;
    private final Integer rating;
    private final List<String> comments;
    private final Date releaseDate;

    private FindUserBookStateResponse(Builder builder) {
        authorId = builder.authorId;
        authorName = builder.authorName;
        authorSurname = builder.authorSurname;
        bookId = builder.bookId;
        title = builder.title;
        about = builder.about;
        year = builder.year;
        numberOfPages = builder.numberOfPages;
        coverPath = builder.coverPath;
        userBookStateId = builder.userBookStateId;
        read = builder.read;
        rating = builder.rating;
        comments = builder.comments;
        releaseDate = builder.releaseDate;
    }


    public static final class Builder {
        private Integer authorId;
        private String authorName;
        private String authorSurname;
        private Integer bookId;
        private String title;
        private String about;
        private Integer year;
        private Integer numberOfPages;
        private String coverPath;
        private Integer userBookStateId;
        private Read read;
        private Integer rating;
        private List<String> comments;
        private Date releaseDate;

        public Builder() {
        }

        public Builder authorId(Integer val) {
            authorId = val;
            return this;
        }

        public Builder authorName(String val) {
            authorName = val;
            return this;
        }

        public Builder authorSurname(String val) {
            authorSurname = val;
            return this;
        }

        public Builder bookId(Integer val) {
            bookId = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder about(String val) {
            about = val;
            return this;
        }

        public Builder year(Integer val) {
            year = val;
            return this;
        }

        public Builder numberOfPages(Integer val) {
            numberOfPages = val;
            return this;
        }

        public Builder coverPath(String val) {
            coverPath = val;
            return this;
        }

        public Builder userBookStateId(Integer val) {
            userBookStateId = val;
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

        public FindUserBookStateResponse build() {
            return new FindUserBookStateResponse(this);
        }
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public Integer getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAbout() {
        return about;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public Integer getUserBookStateId() {
        return userBookStateId;
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
