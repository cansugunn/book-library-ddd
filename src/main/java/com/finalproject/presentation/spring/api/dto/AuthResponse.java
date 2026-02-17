package com.finalproject.presentation.spring.api.dto;

public record AuthResponse(Integer userId, String username, String userType, String token) {
    }