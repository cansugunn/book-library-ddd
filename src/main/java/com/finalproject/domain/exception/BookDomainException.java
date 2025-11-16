package com.finalproject.domain.exception;

public class BookDomainException extends DomainException {
    public BookDomainException(String message) {
        super(message);
    }

    public BookDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
