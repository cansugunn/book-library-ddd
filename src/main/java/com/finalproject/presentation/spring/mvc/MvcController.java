package com.finalproject.presentation.spring.mvc;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.application.ports.input.services.UserApplicationService;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mvc")
public class MvcController {
    public static final String SESSION_USER_KEY = "MVC_AUTH_USER";

    private final UserApplicationService userApplicationService;
    private final BookQueryApplicationService bookQueryApplicationService;
    private final UserBookStateApplicationService userBookStateApplicationService;
    private final MvcUserContextRunner mvcUserContextRunner;

    public MvcController(UserApplicationService userApplicationService,
                         BookQueryApplicationService bookQueryApplicationService,
                         UserBookStateApplicationService userBookStateApplicationService,
                         MvcUserContextRunner mvcUserContextRunner) {
        this.userApplicationService = userApplicationService;
        this.bookQueryApplicationService = bookQueryApplicationService;
        this.userBookStateApplicationService = userBookStateApplicationService;
        this.mvcUserContextRunner = mvcUserContextRunner;
    }

    @GetMapping({"", "/"})
    public String mvcRoot(HttpSession session) {
        return isLoggedIn(session) ? "redirect:/mvc/books" : "redirect:/mvc/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        return isLoggedIn(session) ? "redirect:/mvc/books" : "mvc/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        try {
            FindUserResponse user = userApplicationService.findUser(username, password);
            session.setAttribute(SESSION_USER_KEY, new MvcSessionUser(user.getId(), user.getUsername(), user.getUserType()));
            return "redirect:/mvc/books";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/mvc/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/mvc/login";
    }

    @GetMapping("/books")
    public String books(@PageableDefault(size = 8) Pageable pageable,
                        HttpSession session,
                        Model model) {
        MvcSessionUser user = requireUser(session);
        PageResult<FindBookResponse> result = mvcUserContextRunner.runAs(user,
                () -> bookQueryApplicationService.findAllBooks(new PageQuery(pageable.getPageNumber(), pageable.getPageSize())));

        model.addAttribute("user", user);
        model.addAttribute("booksPage", result);
        return "mvc/books/list";
    }

    @GetMapping("/books/{bookId}")
    public String bookDetails(@PathVariable int bookId,
                              HttpSession session,
                              Model model) {
        MvcSessionUser user = requireUser(session);
        FindBookResponse book = mvcUserContextRunner.runAs(user,
                () -> bookQueryApplicationService.findBook(new com.finalproject.application.dto.book.query.GetBookQuery(bookId)));
        FindUserBookStateResponse state = mvcUserContextRunner.runAs(user,
                () -> userBookStateApplicationService.findUserBookOfCurrentUser(bookId));

        model.addAttribute("user", user);
        model.addAttribute("book", book);
        model.addAttribute("bookState", state);
        return "mvc/books/details";
    }

    @GetMapping("/users/me")
    public String myUserPage(HttpSession session, Model model) {
        MvcSessionUser user = requireUser(session);
        List<FindUserBookStateResponse> favourites = mvcUserContextRunner.runAs(user,
                userBookStateApplicationService::findFavouriteBooksOfCurrentUser);

        model.addAttribute("user", user);
        model.addAttribute("favourites", favourites);
        return "mvc/users/me";
    }

    @GetMapping("/users/me/comments")
    public String myComments(HttpSession session, Model model) {
        MvcSessionUser user = requireUser(session);
        List<FindUserBookStateResponse> favourites = mvcUserContextRunner.runAs(user,
                userBookStateApplicationService::findFavouriteBooksOfCurrentUser);

        model.addAttribute("user", user);
        model.addAttribute("favourites", favourites);
        return "mvc/users/comments";
    }

    @GetMapping("/users/me/favourites")
    public String myFavourites(HttpSession session, Model model) {
        return myUserPage(session, model);
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_KEY) instanceof MvcSessionUser;
    }

    private MvcSessionUser requireUser(HttpSession session) {
        Object sessionValue = session.getAttribute(SESSION_USER_KEY);
        if (sessionValue instanceof MvcSessionUser user) {
            return user;
        }
        throw new MvcUnauthorizedException();
    }
}
