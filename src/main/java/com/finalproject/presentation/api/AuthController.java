package com.finalproject.presentation.api;

import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.application.dto.auth.LoginCommand;
import com.finalproject.application.ports.input.services.UserApplicationService;
import com.finalproject.infrastructure.security.UserContext;
import com.finalproject.infrastructure.security.UserContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserApplicationService userApplicationService;

    public AuthController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginCommand command) {
        FindUserResponse response = userApplicationService.findUser(command.username(), command.password());
        UserContextHolder.set(new UserContext(response.getId(), response.getUsername(), response.getUserType()));
        return new AuthResponse(response.getId(), response.getUsername(), response.getUserType().name());
    }

    public record AuthResponse(Integer userId, String username, String userType) {
    }
}
