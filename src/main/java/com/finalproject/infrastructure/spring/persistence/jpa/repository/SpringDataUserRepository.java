package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Integer> {
    Optional<UserJpaEntity> findByUsernameAndPassword(String username, String password);
}
