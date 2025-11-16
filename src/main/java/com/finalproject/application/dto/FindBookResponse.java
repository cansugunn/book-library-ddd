package com.finalproject.application.dto;

public class FindBookResponse {
    private final Integer authorId;
    private final String authorName;
    private final String authorSurname;
    private final Integer bookId;
    private final String title;
    private final String about;
    private final Integer year;
    private final Integer numberOfPages;
    private final String coverPath;

    private FindBookResponse(Builder builder) {
        authorId = builder.authorId;
        authorName = builder.authorName;
        authorSurname = builder.authorSurname;
        bookId = builder.bookId;
        title = builder.title;
        about = builder.about;
        year = builder.year;
        numberOfPages = builder.numberOfPages;
        coverPath = builder.coverPath;
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

        public Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder authorId(Integer authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder authorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public Builder authorSurname(String authorSurname) {
            this.authorSurname = authorSurname;
            return this;
        }

        public Builder bookId(Integer bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder about(String about) {
            this.about = about;
            return this;
        }

        public Builder year(Integer year) {
            this.year = year;
            return this;
        }

        public Builder numberOfPages(Integer numberOfPages) {
            this.numberOfPages = numberOfPages;
            return this;
        }

        public Builder coverPath(String coverPath) {
            this.coverPath = coverPath;
            return this;
        }

        public FindBookResponse build() {
            return new FindBookResponse(this);
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
}
