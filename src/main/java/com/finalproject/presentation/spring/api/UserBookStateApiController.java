package com.finalproject.presentation.spring.api;

import com.finalproject.application.dto.*;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-book-states")
public class UserBookStateApiController {
    private final UserBookStateApplicationService service;

    public UserBookStateApiController(UserBookStateApplicationService service) {
        this.service = service;
    }

    @GetMapping("/{bookId}")
    public FindUserBookStateResponse findForCurrentUser(@PathVariable int bookId) {
        return service.findUserBookOfCurrentUser(bookId);
    }

    @GetMapping("/favourites")
    public List<FindUserBookStateResponse> favourites() {
        return service.findFavouriteBooksOfCurrentUser();
    }

    @GetMapping("/not-read")
    public List<FindUserBookStateResponse> notRead() {
        return service.findNotReadBooksYetOfCurrentUser();
    }

    @GetMapping("/wished-week")
    public List<FindUserBookStateResponse> wishedWeek() {
        return service.findWishedBooksToReadThatWillBeDoneIn1WeekOfCurrentUser();
    }

    @PostMapping
    public CreateUserBookStateResponse create(@Valid @RequestBody CreateUserBookStateRequest request) {
        return service.createUserBookForCurrentUser(request);
    }

    @PutMapping("/{id}")
    public UpdateUserBookStateResponse update(@PathVariable int id,
                                               @Valid @RequestBody UpdateUserBookStateRequest request) {
        UpdateUserBookStateRequest mapped = new UpdateUserBookStateRequest.Builder()
                .id(id)
                .read(request.getRead())
                .rating(request.getRating())
                .releaseDate(request.getReleaseDate())
                .comments(request.getComments())
                .build();
        return service.updateUserBookForCurrentUser(mapped);
    }
}
