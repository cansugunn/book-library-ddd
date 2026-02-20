package com.finalproject.presentation.spring.mvc;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.dto.book.query.SearchBooksQuery;
import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mvc/books")
public class BookMvcController {
    private final BookQueryApplicationService bookQueryApplicationService;
    private final UserBookStateApplicationService userBookStateApplicationService;

    @GetMapping("/{bookId}")
    public String bookDetails(@PathVariable int bookId, Model model) {
        FindBookResponse book = bookQueryApplicationService.find(new GetBookQuery(bookId));
        FindUserBookStateResponse state = userBookStateApplicationService.findUserBookOfCurrentUser(bookId);

        model.addAttribute("book", book);
        model.addAttribute("bookState", state);

        return "mvc/books/details";
    }

    @GetMapping
    public String home(@RequestParam(required = false) String keyword, Model model) {
        PageResult<FindBookResponse> trending =
                bookQueryApplicationService.findAll(
                        new SearchBooksQuery(keyword, new PageQuery(0, 10)));

        model.addAttribute("keyword", keyword);
        model.addAttribute("trendingBooks", trending.content());

        return "mvc/books/home";
    }
}
