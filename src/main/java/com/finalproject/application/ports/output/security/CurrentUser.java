package com.finalproject.application.ports.output.security;

import com.finalproject.domain.valueobject.UserType;

public interface CurrentUser {
    int getId();
    String getUsername();
    UserType getUsertype();
    boolean isAdmin();
}
