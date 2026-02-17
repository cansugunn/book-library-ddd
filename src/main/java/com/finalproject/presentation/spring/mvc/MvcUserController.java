package com.finalproject.presentation.spring.mvc;

import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mvc/users/me")
public class MvcUserController {
    private final UserBookStateApplicationService userBookStateApplicationService;
    private final MvcUserContextRunner mvcUserContextRunner;
    private final MvcSessionService mvcSessionService;

    public MvcUserController(UserBookStateApplicationService userBookStateApplicationService,
                             MvcUserContextRunner mvcUserContextRunner,
                             MvcSessionService mvcSessionService) {
        this.userBookStateApplicationService = userBookStateApplicationService;
        this.mvcUserContextRunner = mvcUserContextRunner;
        this.mvcSessionService = mvcSessionService;
    }

    @GetMapping
    public String myUserPage(HttpSession session, Model model) {
        MvcSessionUser user = mvcSessionService.require(session);
        List<FindUserBookStateResponse> favourites = mvcUserContextRunner.runAs(user,
                userBookStateApplicationService::findFavouriteBooksOfCurrentUser);

        model.addAttribute("user", user);
        model.addAttribute("favourites", favourites);
        return "mvc/users/me";
    }

    @GetMapping("/comments")
    public String myComments(HttpSession session, Model model) {
        MvcSessionUser user = mvcSessionService.require(session);
        List<FindUserBookStateResponse> favourites = mvcUserContextRunner.runAs(user,
                userBookStateApplicationService::findFavouriteBooksOfCurrentUser);

        model.addAttribute("user", user);
        model.addAttribute("commentStates", favourites.stream().filter(it -> it.getComments() != null && !it.getComments().isEmpty()).toList());
        return "mvc/users/comments";
    }

    @GetMapping("/favourites")
    public String myFavourites(HttpSession session, Model model) {
        return myUserPage(session, model);
    }
}
