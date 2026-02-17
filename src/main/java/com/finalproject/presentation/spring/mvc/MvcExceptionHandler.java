package com.finalproject.presentation.spring.mvc;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(basePackageClasses = {MvcHomeController.class, MvcUserController.class, MvcBookController.class})
public class MvcExceptionHandler {

    @ExceptionHandler(MvcUnauthorizedException.class)
    public String unauthorized(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Please login first");
        return "redirect:/mvc/login";
    }
}
