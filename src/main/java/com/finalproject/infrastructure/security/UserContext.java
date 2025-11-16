package com.finalproject.infrastructure.security;

import com.finalproject.domain.valueobject.UserType;

public class UserContext {
    private final int userId;
    private final String username;
    private final UserType userType;

    public UserContext(int userId, String username, UserType userType) {
        this.userId = userId;
        this.username = username;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }
}
