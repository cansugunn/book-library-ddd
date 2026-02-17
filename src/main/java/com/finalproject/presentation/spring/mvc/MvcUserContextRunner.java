package com.finalproject.presentation.spring.mvc;

import com.finalproject.infrastructure.common.security.UserContext;
import com.finalproject.infrastructure.common.security.UserContextHolder;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class MvcUserContextRunner {

    public <T> T runAs(MvcSessionUser sessionUser, Supplier<T> action) {
        try {
            UserContextHolder.set(new UserContext(sessionUser.id(), sessionUser.username(), sessionUser.userType()));
            return action.get();
        } finally {
            UserContextHolder.clear();
        }
    }
}
