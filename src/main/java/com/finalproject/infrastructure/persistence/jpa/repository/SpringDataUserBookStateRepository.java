package com.finalproject.infrastructure.persistence.jpa.repository;

import com.finalproject.infrastructure.persistence.jpa.entity.UserBookStateJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SpringDataUserBookStateRepository extends JpaRepository<UserBookStateJpaEntity, Integer> {
    Optional<UserBookStateJpaEntity> findByBookIdAndUserId(Integer bookId, Integer userId);

    List<UserBookStateJpaEntity> findByUserIdAndRatingGreaterThan(Integer userId, Integer rating);

    List<UserBookStateJpaEntity> findByUserIdAndReadStatusNot(Integer userId, Integer readStatus);

    @Query("""
            select u from UserBookStateJpaEntity u
            where u.userId = :userId
              and u.readStatus = :wishStatus
              and u.releaseDate <= :weekDate
            """)
    List<UserBookStateJpaEntity> findWishToReadInWeek(Integer userId, Integer wishStatus, Date weekDate);

    void deleteByBookId(Integer bookId);
}
