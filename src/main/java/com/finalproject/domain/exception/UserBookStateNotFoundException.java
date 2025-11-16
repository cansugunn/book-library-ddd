package com.finalproject.domain.exception;

public class UserBookStateNotFoundException extends UserBookStateDomainException{
    public UserBookStateNotFoundException(String message) {
        super(message);
    }

    public UserBookStateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
