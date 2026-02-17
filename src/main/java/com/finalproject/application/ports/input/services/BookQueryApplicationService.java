package com.finalproject.application.ports.input.services;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;

public interface BookQueryApplicationService {
    FindBookResponse findBook(GetBookQuery query);
}
