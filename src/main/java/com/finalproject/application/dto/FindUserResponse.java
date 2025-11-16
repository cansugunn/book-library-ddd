package com.finalproject.application.dto;

import com.finalproject.domain.valueobject.UserType;

public class FindUserResponse {
    private final Integer id;
    private final String username;
    private final UserType userType;

    public FindUserResponse(Builder builder) {
        id = builder.id;
        username = builder.username;
        userType = builder.userType;
    }

    public static final class Builder {
        private Integer id;
        private String username;
        private UserType userType;

        public Builder() {
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder userType(UserType val) {
            userType = val;
            return this;
        }

        public FindUserResponse build() {
            return new FindUserResponse(this);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }
}
