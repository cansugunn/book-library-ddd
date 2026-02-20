package com.finalproject.infrastructure.spring.security.usercontext;

import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.domain.valueobject.UserType;
import com.finalproject.infrastructure.common.security.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class SpringSecurityUserContext implements CurrentUser {
    @Override
    public int getId() {
        return getUserContext()
                .map(UserContext::getUserId)
                .orElse(-1);
    }

    @Override
    public String getUsername() {
        return getUserContext()
                .map(UserContext::getUsername)
                .orElse(null);
    }

    @Override
    public UserType getUsertype() {
        return getUserContext()
                .map(UserContext::getUserType)
                .orElse(null);
    }

    @Override
    public boolean isAdmin() {
        return getUserContext()
                .map(UserContext::getUserType)
                .map(userType -> Objects.equals(userType, UserType.ADMIN))
                .orElse(false);
    }

    private Optional<UserContext> getUserContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserContext p)) {
            return Optional.empty();
        }
        return Optional.of(p);
    }
}