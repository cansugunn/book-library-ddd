package com.finalproject.application.ports.input.services;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.dto.book.query.SearchBooksQuery;
import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;

public interface BookQueryApplicationService {
    FindBookResponse findBook(GetBookQuery query);

    PageResult<FindBookResponse> findAllBooks(PageQuery pageQuery);

    PageResult<FindBookResponse> searchBooks(SearchBooksQuery query);
}
