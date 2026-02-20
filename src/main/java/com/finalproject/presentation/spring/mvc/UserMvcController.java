package com.finalproject.presentation.spring.mvc;

import com.finalproject.application.dto.FindUserBookStateResponse;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mvc/users/me")
public class UserMvcController {
    private final UserBookStateApplicationService userBookStateApplicationService;

    @GetMapping
    public String myUserPage(Model model) {
        List<FindUserBookStateResponse> favourites = userBookStateApplicationService.findFavouriteBooksOfCurrentUser();
        model.addAttribute("favourites", favourites);

        return "mvc/users/me";
    }

    //todo
    @GetMapping("/comments")
    public String myComments(Model model) {
        List<FindUserBookStateResponse> favourites = userBookStateApplicationService.findFavouriteBooksOfCurrentUser();
        model.addAttribute("commentStates", favourites.stream().filter(it -> it.getComments() != null && !it.getComments().isEmpty()).toList());
        return "mvc/users/comments";
    }

    @GetMapping("/favourites")
    public String myFavourites(Model model) {
        return myUserPage(model);
    }
}
