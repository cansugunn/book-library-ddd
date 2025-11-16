package com.finalproject.domain.exception;

public class AuthorDomainException extends DomainException {
    public AuthorDomainException(String message) {
        super(message);
    }

    public AuthorDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
