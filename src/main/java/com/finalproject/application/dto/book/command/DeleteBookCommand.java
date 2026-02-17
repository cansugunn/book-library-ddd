package com.finalproject.application.dto.book.command;

import jakarta.validation.constraints.Min;

public record DeleteBookCommand(@Min(1) int bookId) {
}
