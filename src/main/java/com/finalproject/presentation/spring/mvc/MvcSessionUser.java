package com.finalproject.presentation.spring.mvc;

import com.finalproject.domain.valueobject.UserType;

public record MvcSessionUser(int id, String username, UserType userType) {
}
