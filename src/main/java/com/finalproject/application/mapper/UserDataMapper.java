package com.finalproject.application.mapper;

import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.domain.entity.User;
import com.finalproject.domain.valueobject.UserId;

import java.util.Optional;

public class UserDataMapper {
    public FindUserResponse toFindUserResponse(User user) {
        return new FindUserResponse.Builder()
                .id(Optional.ofNullable(user.getId())
                        .map(UserId::getValue)
                        .orElse(null))
                .username(user.getUsername())
                .userType(user.getUserType())
                .build();
    }
}
