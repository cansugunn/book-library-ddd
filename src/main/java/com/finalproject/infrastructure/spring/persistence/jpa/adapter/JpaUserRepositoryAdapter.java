package com.finalproject.infrastructure.spring.persistence.jpa.adapter;

import com.finalproject.application.ports.output.repository.UserRepository;
import com.finalproject.domain.entity.User;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.domain.valueobject.UserType;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.UserJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaUserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository repository;

    public JpaUserRepositoryAdapter(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return repository.findById(userId.getValue()).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return repository.findByUsernameAndPassword(username, password).map(this::toDomain);
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(
                new UserId(entity.getId()),
                entity.getUsername(),
                entity.getPassword(),
                UserType.of(entity.getType())
        );
    }
}
