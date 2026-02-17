package com.finalproject.infrastructure.spring.persistence.jpa.mapper;

import com.finalproject.domain.entity.User;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.domain.valueobject.UserType;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserJpaMapper {
    public User toDomain(UserJpaEntity entity) {
        return new User(
                new UserId(entity.getId()),
                entity.getUsername(),
                entity.getPassword(),
                UserType.of(entity.getType()));
    }
}
