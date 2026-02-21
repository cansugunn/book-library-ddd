package com.finalproject.infrastructure.spring.persistence.jpa.adapter;

import com.finalproject.application.ports.output.repository.UserBookStateRepository;
import com.finalproject.application.projection.UserBookStatisticsProjection;
import com.finalproject.domain.entity.UserBookState;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.spring.persistence.jpa.mapper.UserBookStateJpaMapper;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.UserBookStateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserBookStateJpaRepositoryAdapter implements UserBookStateRepository {
    private final UserBookStateJpaRepository userBookStateJpaRepository;
    private final UserBookStateJpaMapper userBookStateJpaMapper;

    @Override
    public Optional<UserBookStatisticsProjection> findStatisticsByBookId(BookId bookId) {
        return userBookStateJpaRepository.findStatisticsByBookId(bookId.getValue());
    }

    @Override
    public Optional<UserBookState> findByBookIdAndUserId(BookId bookId, UserId userId) {
        return userBookStateJpaRepository.findByBook_IdAndUser_Id(bookId.getValue(), userId.getValue())
                .map(userBookStateJpaMapper::toDomain);
    }

    @Override
    public List<UserBookState> findRatedOver(UserId userId, Rating rating) {
        return userBookStateJpaRepository.findByUser_IdAndRatingGreaterThan(userId.getValue(), rating.getValue())
                .stream()
                .map(userBookStateJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<UserBookState> findNotReadYetOf(UserId userId) {
        return userBookStateJpaRepository.findByUser_IdAndReadStatusNot(userId.getValue(), Read.READ.getOrdinaryValue())
                .stream()
                .map(userBookStateJpaMapper::toDomain)
                .toList();
    }

    //todo
    @Override
    public List<UserBookState> findToBeReadIn1Week(UserId userId) {
        Date sevenDaysLaterDate = Date.from(LocalDateTime.now()
                .plusDays(7)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        return userBookStateJpaRepository
                .findBy(userId.getValue(), Read.WISH_TO_BE_READ.getOrdinaryValue(), sevenDaysLaterDate)
                .stream()
                .map(userBookStateJpaMapper::toDomain)
                .toList();
    }

    @Override
    public UserBookState save(UserBookState userBookState) {
        return userBookStateJpaMapper.toDomain(
                userBookStateJpaRepository.save(
                        userBookStateJpaMapper.toEntity(userBookState)));
    }

    @Override
    public UserBookState update(UserBookState userBookState) {
        return userBookStateJpaMapper.toDomain(
                userBookStateJpaRepository.save(
                        userBookStateJpaMapper.toEntity(userBookState)));
    }

    @Override
    public void deleteByBookId(BookId bookId) {
        userBookStateJpaRepository.deleteByBook_Id(bookId.getValue());
    }

    @Override
    public boolean exists(UserBookStateId userBookStateId) {
        return userBookStateJpaRepository.existsById(userBookStateId.getValue());
    }

    @Override
    public Optional<UserBookState> findById(UserBookStateId userBookStateId) {
        return userBookStateJpaRepository.findById(userBookStateId.getValue())
                .map(userBookStateJpaMapper::toDomain);
    }
}
