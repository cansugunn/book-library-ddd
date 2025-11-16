package com.finalproject.infrastructure.security;

import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.domain.valueobject.UserType;

public class ThreadLocalCurrentUser implements CurrentUser {
    @Override
    public int getId() {
        return UserContextHolder.get().getUserId();
    }

    @Override
    public String getUsername() {
        return UserContextHolder.get().getUsername();
    }

    @Override
    public UserType getUsertype() {
        return UserContextHolder.get().getUserType();
    }

    @Override
    public boolean isAdmin() {
        return UserContextHolder.get().getUserType().equals(UserType.ADMIN);
    }
}
