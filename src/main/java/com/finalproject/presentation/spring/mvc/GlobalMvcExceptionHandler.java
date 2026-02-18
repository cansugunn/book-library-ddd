package com.finalproject.presentation.spring.mvc;

import com.finalproject.domain.exception.AuthorNotFoundException;
import com.finalproject.domain.exception.BookNotFoundException;
import com.finalproject.domain.exception.DomainException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(basePackageClasses = MvcViewController.class)
public class GlobalMvcExceptionHandler {

    @ExceptionHandler(MvcUnauthorizedException.class)
    public String unauthorized(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Please login first");
        return "redirect:/mvc/login";
    }

    @ExceptionHandler({BookNotFoundException.class, AuthorNotFoundException.class})
    public String notFound(DomainException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", exception.getMessage());
        return "redirect:/mvc";
    }

    @ExceptionHandler(DomainException.class)
    public String domainError(DomainException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", exception.getMessage());
        return "redirect:/mvc";
    }
}
