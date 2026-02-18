package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.AuthorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaAuthorRepository extends JpaRepository<AuthorJpaEntity, Integer> {
    boolean existsByNameAndSurname(String name, String surname);

    List<AuthorJpaEntity> findByNameIgnoreCase(String name);

    Optional<AuthorJpaEntity> findByNameAndSurname(String name, String surname);

    void deleteByNameAndSurname(String name, String surname);

    @Query("""
            select a from AuthorJpaEntity a
            where exists (
                select 1 from BookJpaEntity b
                where b.author.id = a.id and b.id = :bookId
            )
            """)
    Optional<AuthorJpaEntity> findByBookId(Integer bookId);

    @Query("""
            select (count(b) > 0) from BookJpaEntity b
            where b.author.id = :authorId
            """)
    boolean hasMoreBooks(Integer authorId);

    @Query("""
            select (count(b) > 0) from BookJpaEntity b
            where b.author.id = :authorId and b.id <> :bookId
            """)
    boolean hasMoreBooksExcluding(Integer authorId, Integer bookId);
}
