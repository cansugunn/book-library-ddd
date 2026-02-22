package com.finalproject.presentation.spring.api.exceptionhandler;

public record FieldErrorResponse(String field,
                                 String message) {

}