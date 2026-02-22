package com.finalproject.presentation.spring.api;

import com.finalproject.application.dto.*;
import com.finalproject.application.ports.input.services.UserBookStateApplicationService;
import com.finalproject.domain.valueobject.Read;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-book-states")
public class UserBookStateApiController {
    private final UserBookStateApplicationService service;

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
    public CreateUserBookStateResponse
    create(@Valid @RequestBody com.finalproject.presentation.spring.api.dto.CreateUserBookStateRequest request) {
        CreateUserBookStateRequest mapped = new CreateUserBookStateRequest.Builder()
                .bookId(request.bookId())
                .read(Read.valueOf(request.read()))
                .rating(request.rating())
                .releaseDate(request.releaseDate())
                .comments(request.comments())
                .build();
        return service.createUserBookForCurrentUser(mapped);
    }

    @PutMapping("/{id}")
    public UpdateUserBookStateResponse
    update(@PathVariable int id,
           @Valid @RequestBody com.finalproject.presentation.spring.api.dto.UpdateUserBookStateRequest request) {
        UpdateUserBookStateRequest mapped = new UpdateUserBookStateRequest.Builder()
                .id(id)
                .read(Read.valueOf(request.read()))
                .rating(request.rating())
                .releaseDate(request.releaseDate())
                .comments(request.comments())
                .build();
        return service.updateUserBookForCurrentUser(mapped);
    }
}
