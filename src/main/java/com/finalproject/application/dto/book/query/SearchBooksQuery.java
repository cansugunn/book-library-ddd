package com.finalproject.application.dto.book.query;

import com.finalproject.application.dto.page.PageQuery;

public record SearchBooksQuery(String keyword, PageQuery pageQuery) {
}
