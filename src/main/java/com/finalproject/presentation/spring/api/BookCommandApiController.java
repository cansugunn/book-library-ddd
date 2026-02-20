package com.finalproject.presentation.spring.api;

import com.finalproject.application.dto.CreateBookResponse;
import com.finalproject.application.dto.UpdateBookResponse;
import com.finalproject.application.dto.book.command.CreateBookCommand;
import com.finalproject.application.dto.book.command.DeleteBookCommand;
import com.finalproject.application.dto.book.command.UpdateBookCommand;
import com.finalproject.application.ports.input.services.BookCommandApplicationService;
import com.finalproject.presentation.spring.api.dto.CreateBookRequest;
import com.finalproject.presentation.spring.api.dto.UpdateBookRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookCommandApiController {
    private final BookCommandApplicationService commandService;

    public BookCommandApiController(BookCommandApplicationService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateBookResponse createBook(@Valid @RequestBody CreateBookRequest request) {
        return commandService.createBook(new CreateBookCommand(
                request.authorName(),
                request.authorSurname(),
                request.title(),
                request.year(),
                request.numberOfPages(),
                request.about(),
                request.coverPath()));
    }

    @PutMapping("/{bookId}")
    public UpdateBookResponse updateBook(@PathVariable @Min(1) int bookId,
                                         @Valid @RequestBody UpdateBookRequest request) {
        return commandService.updateBook(new UpdateBookCommand(
                bookId,
                request.authorName(),
                request.authorSurname(),
                request.title(),
                request.year(),
                request.numberOfPages(),
                request.about(),
                request.coverPath()));
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable @Min(1) int bookId) {
        commandService.deleteBook(new DeleteBookCommand(bookId));
    }
}
