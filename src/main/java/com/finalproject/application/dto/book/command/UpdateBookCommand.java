package com.finalproject.application.dto.book.command;

public record UpdateBookCommand(
        Integer bookId,
        String authorName,
        String authorSurname,
        String title,
        Integer year,
        Integer numberOfPages,
        String about,
        String coverPath
) {
}
