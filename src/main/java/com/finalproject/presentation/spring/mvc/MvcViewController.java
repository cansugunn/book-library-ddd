package com.finalproject.presentation.spring.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcViewController {

    @GetMapping("/")
    public String root() {
        return "redirect:/mvc";
    }
}
