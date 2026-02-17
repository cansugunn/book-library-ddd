package com.finalproject.presentation.api;

import com.finalproject.application.dto.CreateBookResponse;
import com.finalproject.application.dto.UpdateBookResponse;
import com.finalproject.application.dto.book.command.CreateBookCommand;
import com.finalproject.application.dto.book.command.DeleteBookCommand;
import com.finalproject.application.dto.book.command.UpdateBookCommand;
import com.finalproject.application.ports.input.services.BookCommandApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookCommandController {
    private final BookCommandApplicationService commandService;

    public BookCommandController(BookCommandApplicationService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateBookResponse createBook(@Valid @RequestBody CreateBookCommand command) {
        return commandService.createBook(command);
    }

    @PutMapping("/{bookId}")
    public UpdateBookResponse updateBook(@PathVariable int bookId,
                                         @Valid @RequestBody UpdateBookBody body) {
        return commandService.updateBook(new UpdateBookCommand(
                bookId,
                body.authorName(),
                body.authorSurname(),
                body.title(),
                body.year(),
                body.numberOfPages(),
                body.about(),
                body.coverPath()));
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable int bookId) {
        commandService.deleteBook(new DeleteBookCommand(bookId));
    }

    public record UpdateBookBody(String authorName,
                                 String authorSurname,
                                 String title,
                                 Integer year,
                                 Integer numberOfPages,
                                 String about,
                                 String coverPath) {
    }
}
