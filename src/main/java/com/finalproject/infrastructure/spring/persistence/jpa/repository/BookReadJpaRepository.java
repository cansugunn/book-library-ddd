package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookReadJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookReadJpaRepository extends JpaRepository<BookReadJpaEntity, Integer> {
    Page<BookReadJpaEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("""
                SELECT b
                FROM BookReadJpaEntity b
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM UserBookStateJpaEntity u
                    WHERE u.book.id = b.id
                      AND u.user.id = :userId
                )
            """)
    List<BookReadJpaEntity> findBooksWithoutUserBookStateRecords(Integer userId);
}
