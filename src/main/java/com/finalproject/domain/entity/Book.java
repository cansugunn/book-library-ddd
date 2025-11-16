package com.finalproject.domain.entity;

import com.finalproject.domain.exception.BookDomainException;
import com.finalproject.domain.valueobject.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class Book extends BaseEntity<BookId> {
    private final AuthorId authorId;
    private String title;
    private Year year;
    private NumberOfPages numberOfPages;
    private Cover cover;
    private String about;

    public Book(BookId id, AuthorId authorId, String title, Year year, NumberOfPages numberOfPages, Cover cover, String about) {
        super(id);
        this.authorId = authorId;
        this.title = title;
        this.year = year;
        this.numberOfPages = numberOfPages;
        this.cover = cover;
        this.about = about;
    }

    private Book(Builder builder) {
        super(builder.id);
        authorId = builder.authorId;
        setTitle(builder.title);
        setYear(builder.year);
        setNumberOfPages(builder.numberOfPages);
        setCover(builder.cover);
        setAbout(builder.about);
    }

    @Override
    public void validate() {
        validateYear();
        validateNumberOfPages();
        validateCover();
    }

    private void validateCover() {
        if (cover == null || cover.getPath() == null) {
            return;
        }

        Path path = Path.of(cover.getPath());
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new BookDomainException("Cover path is invalid or not a regular file!");
        }
    }

    private void validateNumberOfPages() {
        final int maxNumberOfPages = 10_000;
        if (numberOfPages == null || numberOfPages.getValue() < 0 || numberOfPages.getValue() > maxNumberOfPages) {
            throw new BookDomainException("Number of pages must be between %d and %d".formatted(0, maxNumberOfPages));
        }
    }

    private void validateYear() {
        final int minYear = 1000;
        int currentYear = LocalDate.now().getYear();
        if (year == null || year.getValue() < minYear || year.getValue() > currentYear) {
            throw new BookDomainException("Year must be between %d and %d".formatted(minYear, currentYear));
        }
    }

    public AuthorId getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public Year getYear() {
        return year;
    }

    public NumberOfPages getNumberOfPages() {
        return numberOfPages;
    }

    public Cover getCover() {
        return cover;
    }

    public String getAbout() {
        return about;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public void setNumberOfPages(NumberOfPages numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public void setAbout(String about) {
        this.about = about;
    }


    public static final class Builder {
        private BookId id;
        private AuthorId authorId;
        private String title;
        private Year year;
        private NumberOfPages numberOfPages;
        private Cover cover;
        private String about;

        public Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(BookId id) {
            this.id = id;
            return this;
        }

        public Builder author(AuthorId authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder year(Year year) {
            this.year = year;
            return this;
        }

        public Builder numberOfPages(NumberOfPages numberOfPages) {
            this.numberOfPages = numberOfPages;
            return this;
        }

        public Builder cover(Cover cover) {
            this.cover = cover;
            return this;
        }

        public Builder about(String about) {
            this.about = about;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
