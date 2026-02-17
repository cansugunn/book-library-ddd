package com.finalproject.application.dto.book.query;

import com.finalproject.application.dto.page.PageQuery;

import java.util.Optional;

public record SearchBooksQuery(String title, PageQuery pageQuery) {
    public SearchBooksQuery {
        title = Optional.ofNullable(title)
                .map(String::trim)
                .filter(k -> !k.isBlank())
                .orElse(null);
    }
}
