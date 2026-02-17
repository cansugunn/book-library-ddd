package com.finalproject.presentation.spring.mvc;

import com.finalproject.application.dto.FindBookResponse;
import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.application.dto.book.query.GetBookQuery;
import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.ports.input.services.BookQueryApplicationService;
import com.finalproject.application.ports.input.services.UserApplicationService;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import com.finalproject.presentation.spring.mvc.dto.BookCardView;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
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
    public String mvcRoot(@PageableDefault(size = 12) Pageable pageable,
                          HttpSession session,
                          Model model) {
        MvcSessionUser user = getSessionUser(session);
        PageResult<FindBookResponse> result = bookQueryApplicationService
                .findAllBooks(new PageQuery(pageable.getPageNumber(), pageable.getPageSize()));

        model.addAttribute("user", user);
        model.addAttribute("heroBooks", toBookCards(result.content().stream().limit(6).toList()));
        model.addAttribute("booksPage", result);
        model.addAttribute("bookCards", toBookCards(result.content()));
        return "mvc/books/home";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        return getSessionUser(session) != null ? "redirect:/mvc" : "mvc/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        try {
            FindUserResponse user = userApplicationService.findUser(username, password);
            session.setAttribute(SESSION_USER_KEY, new MvcSessionUser(user.getId(), user.getUsername(), user.getUserType()));
            return "redirect:/mvc";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials");
            return "redirect:/mvc/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/mvc";
    }

    @GetMapping("/books/{bookId}")
    public String bookDetails(@PathVariable int bookId,
                              HttpSession session,
                              Model model) {
        MvcSessionUser user = getSessionUser(session);
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

    @GetMapping("/media/cover")
    @ResponseBody
    public ResponseEntity<Resource> mediaCover(@RequestParam("path") String path) {
        try {
            Path cover = Path.of(path).normalize().toAbsolutePath();
            if (!Files.exists(cover) || !Files.isRegularFile(cover)) {
                return ResponseEntity.notFound().build();
            }
            String type = Files.probeContentType(cover);
            MediaType mediaType = type == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(type);
            return ResponseEntity.ok().contentType(mediaType).body(new FileSystemResource(cover));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private List<BookCardView> toBookCards(List<FindBookResponse> books) {
        return books == null ? Collections.emptyList() : books.stream()
                .map(b -> new BookCardView(
                        b.getBookId(),
                        b.getTitle(),
                        (b.getAuthorName() + " " + b.getAuthorSurname()).trim(),
                        b.getAbout(),
                        b.getCoverPath(),
                        b.getYear()
                ))
                .toList();
    }

    private MvcSessionUser getSessionUser(HttpSession session) {
        Object sessionValue = session.getAttribute(SESSION_USER_KEY);
        return sessionValue instanceof MvcSessionUser user ? user : null;
    }

    private MvcSessionUser requireUser(HttpSession session) {
        MvcSessionUser user = getSessionUser(session);
        if (user == null) {
            throw new MvcUnauthorizedException();
        }
        return user;
    }
}
