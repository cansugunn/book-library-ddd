package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaBookRepository extends JpaRepository<BookJpaEntity, Integer> {
    Page<BookJpaEntity> findAll(Pageable pageable);

    Page<BookJpaEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    @Query("""
            select b from BookJpaEntity b
            where not exists (
                select 1 from UserBookStateJpaEntity u
                where u.book.id = b.id and u.user.id = :userId
            )
            """)
    List<BookJpaEntity> findBooksWithoutUserBookStateRecords(Integer userId);
}
