package com.finalproject.presentation.api;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookQueryController {
    private final BookQueryApplicationService queryService;

    public BookQueryController(BookQueryApplicationService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{bookId}")
    public FindBookResponse findBook(@PathVariable int bookId) {
        return queryService.findBook(new GetBookQuery(bookId));
    }
}
