package com.finalproject.application.ports.input.services;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.dto.book.query.SearchBooksQuery;
import com.finalproject.application.dto.page.PageResult;

public interface BookQueryApplicationService {
    FindBookResponse find(GetBookQuery query);

    PageResult<FindBookResponse> findAll(SearchBooksQuery query);
}
