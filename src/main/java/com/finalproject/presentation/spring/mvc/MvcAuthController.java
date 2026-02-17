package com.finalproject.presentation.spring.mvc;

import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.application.ports.input.services.UserApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mvc")
public class MvcAuthController {
    private final UserApplicationService userApplicationService;
    private final MvcSessionService mvcSessionService;

    public MvcAuthController(UserApplicationService userApplicationService,
                             MvcSessionService mvcSessionService) {
        this.userApplicationService = userApplicationService;
        this.mvcSessionService = mvcSessionService;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        return mvcSessionService.get(session) != null ? "redirect:/mvc" : "mvc/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        try {
            FindUserResponse user = userApplicationService.findUser(username, password);
            mvcSessionService.login(session, new MvcSessionUser(user.getId(), user.getUsername(), user.getUserType()));
            return "redirect:/mvc";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials");
            return "redirect:/mvc/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        mvcSessionService.logout(session);
        return "redirect:/mvc";
    }
}
