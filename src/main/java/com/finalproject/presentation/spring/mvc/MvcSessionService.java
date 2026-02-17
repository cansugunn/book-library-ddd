package com.finalproject.presentation.spring.mvc;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class MvcSessionService {
    public static final String SESSION_USER_KEY = "MVC_AUTH_USER";

    public MvcSessionUser get(HttpSession session) {
        Object sessionValue = session.getAttribute(SESSION_USER_KEY);
        return sessionValue instanceof MvcSessionUser user ? user : null;
    }

    public MvcSessionUser require(HttpSession session) {
        MvcSessionUser user = get(session);
        if (user == null) {
            throw new MvcUnauthorizedException();
        }
        return user;
    }

    public void login(HttpSession session, MvcSessionUser user) {
        session.setAttribute(SESSION_USER_KEY, user);
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
