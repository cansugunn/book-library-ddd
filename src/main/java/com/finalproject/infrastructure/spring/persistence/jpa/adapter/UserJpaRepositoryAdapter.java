package com.finalproject.infrastructure.spring.persistence.jpa.adapter;

import com.finalproject.application.ports.output.repository.UserRepository;
import com.finalproject.domain.entity.User;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.infrastructure.spring.persistence.jpa.mapper.UserJpaMapper;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserJpaMapper userJpaMapper;

    @Override
    public Optional<User> findById(UserId userId) {
        return userJpaRepository.findById(userId.getValue()).map(userJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userJpaRepository.findByUsernameAndPassword(username, password).map(userJpaMapper::toDomain);
    }
}
