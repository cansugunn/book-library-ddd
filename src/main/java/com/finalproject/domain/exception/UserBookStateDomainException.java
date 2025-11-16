package com.finalproject.domain.exception;

public class UserBookStateDomainException extends DomainException {
    public UserBookStateDomainException(String message) {
        super(message);
    }

    public UserBookStateDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
