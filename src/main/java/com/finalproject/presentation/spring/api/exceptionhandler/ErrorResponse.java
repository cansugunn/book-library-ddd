package com.finalproject.presentation.spring.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(String message,
                            List<FieldErrorResponse> errors,
                            LocalDateTime timestamp) {
}