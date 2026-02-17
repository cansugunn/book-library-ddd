package com.finalproject.application.dto.book.query;

import jakarta.validation.constraints.Min;

public record GetBookQuery(@Min(1) int bookId) {
}
