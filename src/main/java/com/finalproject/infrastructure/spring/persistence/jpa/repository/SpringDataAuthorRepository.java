package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.AuthorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpringDataAuthorRepository extends JpaRepository<AuthorJpaEntity, Integer> {
    boolean existsByNameAndSurname(String name, String surname);

    List<AuthorJpaEntity> findByNameIgnoreCase(String name);

    Optional<AuthorJpaEntity> findByNameAndSurname(String name, String surname);

    void deleteByNameAndSurname(String name, String surname);

    @Query(value = "select a.* from authors a join books b on b.author_id = a.id where b.id = :bookId", nativeQuery = true)
    Optional<AuthorJpaEntity> findByBookId(Integer bookId);

    @Query(value = "select count(*) > 0 from books where author_id = :authorId", nativeQuery = true)
    boolean hasMoreBooks(Integer authorId);

    @Query(value = "select count(*) > 0 from books where author_id = :authorId and id <> :bookId", nativeQuery = true)
    boolean hasMoreBooksExcluding(Integer authorId, Integer bookId);
}
