package com.finalproject.presentation.api;

import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.application.dto.auth.LoginCommand;
import com.finalproject.application.ports.input.services.UserApplicationService;
import com.finalproject.bootstrap.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserApplicationService userApplicationService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserApplicationService userApplicationService,
                          JwtTokenProvider jwtTokenProvider) {
        this.userApplicationService = userApplicationService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Login and receive JWT token", security = {})
    @SecurityRequirements
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginCommand command) {
        FindUserResponse response = userApplicationService.findUser(command.username(), command.password());
        String token = jwtTokenProvider.generateToken(response.getId(), response.getUsername(), response.getUserType());
        return new AuthResponse(response.getId(), response.getUsername(), response.getUserType().name(), token);
    }

    public record AuthResponse(Integer userId, String username, String userType, String token) {
    }
}
