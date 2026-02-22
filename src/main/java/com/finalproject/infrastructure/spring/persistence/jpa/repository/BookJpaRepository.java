package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookJpaRepository extends JpaRepository<BookJpaEntity, Integer> {
    Page<BookJpaEntity> findAll(Pageable pageable);

    Page<BookJpaEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<BookJpaEntity> findByAuthorId(Integer authorId);

    @Query(value = "select b.* from books b left join user_book_states u on b.id = u.book_id and u.userinfo_id = :userId where u.id is null", nativeQuery = true)
    List<BookJpaEntity> findBooksWithoutUserBookStateRecords(Integer userId);
}
