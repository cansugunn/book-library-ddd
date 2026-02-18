package com.finalproject.infrastructure.spring.persistence.jpa.repository;

import com.finalproject.infrastructure.spring.persistence.jpa.entity.UserBookStateJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface JpaUserBookStateRepository extends JpaRepository<UserBookStateJpaEntity, Integer> {
    Optional<UserBookStateJpaEntity> findByBook_IdAndUser_Id(Integer bookId, Integer userId);

    List<UserBookStateJpaEntity> findByUser_IdAndRatingGreaterThan(Integer userId, Integer rating);

    List<UserBookStateJpaEntity> findByUser_IdAndReadStatusNot(Integer userId, Integer readStatus);

    @Query("""
            select u from UserBookStateJpaEntity u
            where u.user.id = :userId
              and u.readStatus = :wishStatus
              and u.releaseDate <= :weekDate
            """)
    List<UserBookStateJpaEntity> findWishToReadInWeek(Integer userId, Integer wishStatus, Date weekDate);

    void deleteByBook_Id(Integer bookId);
}
