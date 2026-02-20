package com.finalproject.presentation.spring.mvc.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class MvcExceptionHandler {
    @ExceptionHandler(MvcUnauthorizedException.class)
    public String unauthorized(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Please login first");
        return "redirect:/mvc/books";
    }
}
