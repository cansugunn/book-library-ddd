package com.finalproject.application.ports.input.services;

import com.finalproject.application.dto.FindUserResponse;

public interface UserApplicationService {
    FindUserResponse findUser(String username, String password);
    boolean isCurrentUserAdmin();
}
