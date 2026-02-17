package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.AuthorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorJpaRepository extends JpaRepository<AuthorJpaEntity, Integer> {
    boolean existsByNameAndSurname(String name, String surname);

    List<AuthorJpaEntity> findByNameIgnoreCase(String name);

    Optional<AuthorJpaEntity> findByNameAndSurname(String name, String surname);

    @Query("""
            SELECT a
            FROM AuthorJpaEntity a
            JOIN BookJpaEntity b
                ON a = b.author
            JOIN UserBookStateJpaEntity ubs
                ON b = ubs.book
            JOIN ubs.user u
            WHERE u.id = :userId AND ubs.rating >= :rating
            GROUP BY a
            HAVING COUNT(a) >= 3
            """)
    List<AuthorJpaEntity>
    findWhichUserHasAtLeastThreeBooksRatedOver(Integer userId, Integer rating);

    @Query(value = """
            SELECT a
            FROM AuthorJpaEntity a
            JOIN BookJpaEntity b
                ON b.author = a
                AND b.id = :bookId
            """)
    Optional<AuthorJpaEntity> findByBookId(Integer bookId);

    @Query(value = """
            SELECT count(b) > 0
            FROM BookJpaEntity b
            JOIN b.author a
            WHERE a.id = :authorId
            """)
    boolean hasMoreBooks(Integer authorId);

    @Query(value = """
            SELECT count(b) > 0
            FROM BookJpaEntity b
            JOIN b.author a
            WHERE a.id = :authorId
                AND b.id != :bookId
            """)
    boolean hasMoreBooksExcluding(Integer authorId, Integer bookId);
}
