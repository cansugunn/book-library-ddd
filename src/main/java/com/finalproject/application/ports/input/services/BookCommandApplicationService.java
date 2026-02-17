package com.finalproject.application.ports.input.services;

import com.finalproject.application.dto.CreateBookResponse;
import com.finalproject.application.dto.UpdateBookResponse;
import com.finalproject.application.dto.book.command.CreateBookCommand;
import com.finalproject.application.dto.book.command.DeleteBookCommand;
import com.finalproject.application.dto.book.command.UpdateBookCommand;

public interface BookCommandApplicationService {
    CreateBookResponse createBook(CreateBookCommand command);

    UpdateBookResponse updateBook(UpdateBookCommand command);

    void deleteBook(DeleteBookCommand command);
}
