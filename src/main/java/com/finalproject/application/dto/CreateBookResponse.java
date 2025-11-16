package com.finalproject.application.dto;

public class CreateBookResponse {
    private final Integer authorId;
    private final String authorName;
    private final String authorSurname;
    private final Integer bookId;
    private final String title;
    private final Integer year;
    private final Integer numberOfPages;
    private final String about;
    private final String coverPath;

    private CreateBookResponse(Builder builder) {
        authorId = builder.authorId;
        authorName = builder.authorName;
        authorSurname = builder.authorSurname;
        bookId = builder.bookId;
        title = builder.title;
        year = builder.year;
        numberOfPages = builder.numberOfPages;
        about = builder.about;
        coverPath = builder.coverPath;
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

    public Integer getYear() {
        return year;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public String getAbout() {
        return about;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public static final class Builder {
        private Integer authorId;
        private String authorName;
        private String authorSurname;
        private Integer bookId;
        private String title;
        private Integer year;
        private Integer numberOfPages;
        private String about;
        private String coverPath;

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

        public Builder year(Integer val) {
            year = val;
            return this;
        }

        public Builder numberOfPages(Integer val) {
            numberOfPages = val;
            return this;
        }

        public Builder about(String val) {
            about = val;
            return this;
        }

        public Builder coverPath(String val) {
            coverPath = val;
            return this;
        }

        public CreateBookResponse build() {
            return new CreateBookResponse(this);
        }
    }
}
