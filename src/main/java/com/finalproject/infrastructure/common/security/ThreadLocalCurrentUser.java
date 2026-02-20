package com.finalproject.infrastructure.common.security;

import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.domain.exception.UserDomainException;
import com.finalproject.domain.valueobject.UserType;

public class ThreadLocalCurrentUser implements CurrentUser {
    private UserContext requireCurrentUser() {
        UserContext context = UserContextHolder.get();
        if (context == null) {
            throw new UserDomainException("Missing authenticated user context. Provide X-User-* headers or login first.");
        }
        return context;
    }

    @Override
    public int getId() {
        return requireCurrentUser().getUserId();
    }

    @Override
    public String getUsername() {
        return requireCurrentUser().getUsername();
    }

    @Override
    public UserType getUsertype() {
        return requireCurrentUser().getUserType();
    }

    @Override
    public boolean isAdmin() {
        return UserType.ADMIN.equals(requireCurrentUser().getUserType());
    }
}
