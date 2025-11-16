package com.finalproject.infrastructure.security;

public class UserContextHolder {
    //todo make more easy
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static void set(UserContext context) {
        userContext.set(context);
    }

    public static UserContext get() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();
    }
}
