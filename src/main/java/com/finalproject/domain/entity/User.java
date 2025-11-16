package com.finalproject.domain.entity;

import com.finalproject.domain.valueobject.UserId;
import com.finalproject.domain.valueobject.UserType;

//todo rename db tables, and similar thinks
public class User extends BaseEntity<UserId> {
    private final String username;
    private String password;
    private final UserType userType;

    public User(UserId id, String username, String password, UserType userType) {
        super(id);
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    private User(Builder builder) {
        super(builder.id);
        username = builder.username;
        password = builder.password;
        userType = builder.userType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public void validate() {

    }


    public static final class Builder {
        private UserId id;
        private String username;
        private String password;
        private UserType userType;

        public Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(UserId id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder userType(UserType userType) {
            this.userType = userType;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
