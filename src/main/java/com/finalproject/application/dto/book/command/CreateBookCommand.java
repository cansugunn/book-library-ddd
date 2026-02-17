package com.finalproject.application.dto.book.command;

public record CreateBookCommand(
        String authorName,
        String authorSurname,
        String title,
        Integer year,
        Integer numberOfPages,
        String about,
        String coverPath
) {
}
