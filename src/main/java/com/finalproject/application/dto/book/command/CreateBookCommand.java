package com.finalproject.application.dto.book.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookCommand(
        @NotBlank String authorName,
        @NotBlank String authorSurname,
        @NotBlank String title,
        @NotNull @Min(0) Integer year,
        @NotNull @Min(1) Integer numberOfPages,
        @NotBlank String about,
        @NotBlank String coverPath
) {
}
