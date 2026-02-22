package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookReadJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookReadJpaRepository extends JpaRepository<BookReadJpaEntity, Integer> {
    Page<BookReadJpaEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query(value = "select b.* from books_read_model b left join user_book_states u on b.id = u.book_id and u.userinfo_id = :userId where u.id is null", nativeQuery = true)
    List<BookReadJpaEntity> findBooksWithoutUserBookStateRecords(Integer userId);

    List<BookReadJpaEntity> findByAuthorId(Integer authorId);

    @Modifying
    @Query("delete from BookReadJpaEntity b where b.authorId = :authorId")
    void deleteByAuthorId(@Param("authorId") Integer authorId);
}
