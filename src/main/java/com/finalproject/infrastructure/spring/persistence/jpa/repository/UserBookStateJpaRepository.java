package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.application.projection.UserBookStatisticsProjection;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.UserBookStateJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserBookStateJpaRepository extends JpaRepository<UserBookStateJpaEntity, Integer> {
    Optional<UserBookStateJpaEntity> findByBook_IdAndUser_Id(Integer bookId, Integer userId);

    List<UserBookStateJpaEntity> findByUser_IdAndRatingGreaterThan(Integer userId, Integer rating);

    List<UserBookStateJpaEntity> findByUser_IdAndReadStatusNot(Integer userId, Integer readStatus);

    @Query("""
            select u from UserBookStateJpaEntity u
            where u.user.id = :userId
              and u.readStatus = :wishStatus
              and u.releaseDate <= :weekDate
            """)
    List<UserBookStateJpaEntity> findBy(Integer userId, Integer wishStatus, Date weekDate);

    void deleteByBook_Id(Integer bookId);

    @Query("""
            SELECT new com.finalproject.application.projection.UserBookStatisticsProjection(
                (SELECT SUM(CASE WHEN u1.readStatus = 1 THEN 1 ELSE 0 END)
                 FROM UserBookStateJpaEntity u1
                 WHERE u1.book.id = :bookId),
        
                (SELECT COALESCE(AVG(u2.rating), 0)
                 FROM UserBookStateJpaEntity u2
                 WHERE u2.book.id = :bookId
                   AND u2.rating IS NOT NULL),
        
                (SELECT COUNT(c)
                 FROM CommentJpaEntity c
                 JOIN c.userBookState cubs
                 JOIN cubs.book cb
                 WHERE cb.id = :bookId))
            FROM UserBookStateJpaEntity u
            WHERE u.book.id = :bookId
            GROUP BY u.book.id
            """)
    Optional<UserBookStatisticsProjection> findStatisticsByBookId(@Param("bookId") Integer bookId);
}
