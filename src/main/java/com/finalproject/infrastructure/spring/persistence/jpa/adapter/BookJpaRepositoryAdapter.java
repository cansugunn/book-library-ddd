package com.finalproject.infrastructure.spring.persistence.jpa.adapter;

import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.BookId;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookReadJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.mapper.BookJpaMapper;
import com.finalproject.infrastructure.spring.persistence.jpa.mapper.BookReadJpaMapper;
import com.finalproject.infrastructure.spring.persistence.jpa.mapper.PageMapper;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.BookJpaRepository;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.BookReadJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookJpaRepositoryAdapter implements BookRepository {
    private final BookJpaRepository bookJpaRepository;
    private final BookReadJpaRepository bookReadJpaRepository;
    private final BookJpaMapper bookJpaMapper;
    private final BookReadJpaMapper bookReadJpaMapper;
    private final PageMapper pageMapper;

    @Override
    public boolean exists(BookId bookId) {
        return bookJpaRepository.existsById(bookId.getValue());
    }

    @Transactional
    @Override
    public Book save(Book book) {
        BookJpaEntity savedBook = bookJpaRepository.save(bookJpaMapper.toEntity(book));
        bookReadJpaRepository.save(bookReadJpaMapper.toReadEntity(savedBook, savedBook.getAuthor()));
        return bookJpaMapper.toDomain(savedBook);
    }

    @Transactional
    @Override
    public Book update(Book book) {
        BookJpaEntity savedBook = bookJpaRepository.save(bookJpaMapper.toEntity(book));
        bookReadJpaRepository.save(bookReadJpaMapper.toReadEntity(savedBook, savedBook.getAuthor()));
        return bookJpaMapper.toDomain(savedBook);
    }

    @Transactional
    @Override
    public void delete(BookId bookId) {
        bookJpaRepository.deleteById(bookId.getValue());
        bookReadJpaRepository.deleteById(bookId.getValue());
    }

    @Override
    public Optional<Book> findById(BookId bookId) {
        return bookReadJpaRepository.findById(bookId.getValue()).map(bookReadJpaMapper::toDomain);
    }

    @Override
    public List<Book> findBooksWithoutUserBookStateRecords(UserId userId) {
        return bookReadJpaRepository.findBooksWithoutUserBookStateRecords(userId.getValue())
                .stream()
                .map(bookReadJpaMapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<Book> findAll(PageQuery pageQuery) {
        Page<BookReadJpaEntity> page = bookReadJpaRepository.findAll(PageRequest.of(pageQuery.page(), pageQuery.size()));
        return pageMapper.toPageResult(page, bookReadJpaMapper::toDomain);
    }

    @Override
    public PageResult<Book> searchByTitle(String title, PageQuery pageQuery) {
        Page<BookReadJpaEntity> page = bookReadJpaRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(pageQuery.page(), pageQuery.size()));
        return pageMapper.toPageResult(page, bookReadJpaMapper::toDomain);
    }
}
