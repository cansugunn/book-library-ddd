package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<BookJpaEntity, Integer> {
    Page<BookJpaEntity> findAll(Pageable pageable);
}
