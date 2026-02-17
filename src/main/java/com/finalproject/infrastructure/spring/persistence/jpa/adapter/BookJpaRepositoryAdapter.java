package com.finalproject.infrastructure.spring.persistence.jpa.adapter;

import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.BookId;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.mapper.BookJpaMapper;
import com.finalproject.infrastructure.spring.persistence.jpa.mapper.PageMapper;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.BookJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookJpaRepositoryAdapter implements BookRepository {
    private final BookJpaRepository bookJpaRepository;
    private final BookJpaMapper bookJpaMapper;
    private final PageMapper pageMapper;

    @Override
    public boolean exists(BookId bookId) {
        return bookJpaRepository.existsById(bookId.getValue());
    }

    @Override
    public Book save(Book book) {
        return bookJpaMapper.toDomain(bookJpaRepository.save(bookJpaMapper.toEntity(book)));
    }

    @Override
    public Book update(Book book) {
        return bookJpaMapper.toDomain(bookJpaRepository.save(bookJpaMapper.toEntity(book)));
    }

    @Override
    public void delete(BookId bookId) {
        bookJpaRepository.deleteById(bookId.getValue());
    }

    @Override
    public Optional<Book> findById(BookId bookId) {
        return bookJpaRepository.findById(bookId.getValue()).map(bookJpaMapper::toDomain);
    }

    @Override
    public List<Book> findBooksWithoutUserBookStateRecords(UserId userId) {
        return bookJpaRepository.findBooksWithoutUserBookStateRecords(userId.getValue())
                .stream()
                .map(bookJpaMapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<Book> findAll(PageQuery pageQuery) {
        Page<BookJpaEntity> page = bookJpaRepository.findAll(PageRequest.of(pageQuery.page(), pageQuery.size()));
        return pageMapper.toPageResult(page, bookJpaMapper::toDomain);
    }

    @Override
    public PageResult<Book> searchByTitle(String title, PageQuery pageQuery) {
        Page<BookJpaEntity> page = bookJpaRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(pageQuery.page(), pageQuery.size()));
        return pageMapper.toPageResult(page, bookJpaMapper::toDomain);
    }
}
