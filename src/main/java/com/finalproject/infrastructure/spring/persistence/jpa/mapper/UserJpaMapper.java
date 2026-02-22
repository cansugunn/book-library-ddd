package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.User;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.domain.valueobject.UserType;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserJpaMapper {
    public User toDomain(UserJpaEntity entity) {
        return new User.Builder()
                .id(Optional.ofNullable(entity.getId())
                        .map(UserId::new)
                        .orElse(null))
                .username(entity.getUsername())
                .password(entity.getPassword())
                .userType(UserType.of(entity.getType()))
                .build();
    }
}
