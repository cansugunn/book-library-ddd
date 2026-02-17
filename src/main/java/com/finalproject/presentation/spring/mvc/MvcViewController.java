package com.finalproject.presentation.spring.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcViewController {

    @GetMapping({"/", "/mvc", "/mvc/"})
    public String root() {
        return "redirect:/mvc/index.html";
    }
}
