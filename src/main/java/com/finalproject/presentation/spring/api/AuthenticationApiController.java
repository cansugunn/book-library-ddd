package com.finalproject.presentation.spring.api;

import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.application.ports.input.services.UserApplicationService;
import com.finalproject.infrastructure.spring.security.jwt.JwtTokenProvider;
import com.finalproject.presentation.spring.api.dto.AuthResponse;
import com.finalproject.presentation.spring.api.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationApiController {
    private final UserApplicationService userApplicationService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationApiController(UserApplicationService userApplicationService,
                                       JwtTokenProvider jwtTokenProvider) {
        this.userApplicationService = userApplicationService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Login and receive JWT token", security = {})
    @SecurityRequirements
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        FindUserResponse response = userApplicationService.findUser(request.username(), request.password());
        String token = jwtTokenProvider.generateToken(response.getId(), response.getUsername(), response.getUserType());
        return new AuthResponse(response.getId(), response.getUsername(), response.getUserType().name(), token);
    }
}
