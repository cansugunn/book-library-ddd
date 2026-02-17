package com.finalproject.presentation.spring.mvc;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc/books")
public class MvcBookController {
    private final BookQueryApplicationService bookQueryApplicationService;
    private final UserBookStateApplicationService userBookStateApplicationService;
    private final MvcUserContextRunner mvcUserContextRunner;
    private final MvcSessionService mvcSessionService;

    public MvcBookController(BookQueryApplicationService bookQueryApplicationService,
                             UserBookStateApplicationService userBookStateApplicationService,
                             MvcUserContextRunner mvcUserContextRunner,
                             MvcSessionService mvcSessionService) {
        this.bookQueryApplicationService = bookQueryApplicationService;
        this.userBookStateApplicationService = userBookStateApplicationService;
        this.mvcUserContextRunner = mvcUserContextRunner;
        this.mvcSessionService = mvcSessionService;
    }

    @GetMapping("/{bookId}")
    public String bookDetails(@PathVariable int bookId,
                              HttpSession session,
                              Model model) {
        MvcSessionUser user = mvcSessionService.get(session);
        FindBookResponse book = bookQueryApplicationService.findBook(new GetBookQuery(bookId));
        FindUserBookStateResponse state = null;
        if (user != null) {
            state = mvcUserContextRunner.runAs(user,
                    () -> userBookStateApplicationService.findUserBookOfCurrentUser(bookId));
        }

        model.addAttribute("user", user);
        model.addAttribute("book", book);
        model.addAttribute("bookState", state);
        return "mvc/books/details";
    }
}
