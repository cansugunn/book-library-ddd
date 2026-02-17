package com.finalproject.presentation.spring.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookRequest(
        @NotBlank String authorName,
        @NotBlank String authorSurname,
        @NotBlank String title,
        @NotNull @Min(0) Integer year,
        @NotNull @Min(1) Integer numberOfPages,
        @NotBlank String about,
        @NotBlank String coverPath
) {
}
