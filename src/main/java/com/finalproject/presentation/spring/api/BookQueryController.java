package com.finalproject.presentation.spring.api;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.dto.book.query.SearchBooksQuery;
import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookQueryController {
    private final BookQueryApplicationService queryService;

    public BookQueryController(BookQueryApplicationService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{bookId}")
    public FindBookResponse findBook(@PathVariable @Min(1) int bookId) {
        return queryService.findBook(new GetBookQuery(bookId));
    }

    @GetMapping
    public PageResult<FindBookResponse> findBooks(@RequestParam(value = "q", required = false) String keyword,
                                                  @PageableDefault(size = 10) Pageable pageable) {
        return queryService.searchBooks(new SearchBooksQuery(keyword, new PageQuery(pageable.getPageNumber(), pageable.getPageSize())));
    }
}
