package com.finalproject.application.ports.output.repository;

import com.finalproject.domain.entity.User;
import com.finalproject.domain.valueobject.UserId;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(UserId userId);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
