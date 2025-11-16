package com.finalproject.domain.exception;

public class BookNotFoundException extends DomainException {
    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
