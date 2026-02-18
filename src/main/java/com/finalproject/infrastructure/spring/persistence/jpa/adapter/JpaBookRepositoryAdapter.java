package com.finalproject.infrastructure.spring.persistence.jpa.adapter;

import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.BookId;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.mapper.BookJpaMapper;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.JpaBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaBookRepositoryAdapter implements BookRepository {
    private final JpaBookRepository repository;
    private final BookJpaMapper mapper;

    public JpaBookRepositoryAdapter(JpaBookRepository repository,
                                    BookJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean exists(BookId bookId) {
        return repository.existsById(bookId.getValue());
    }

    @Override
    public Book save(Book book) {
        return mapper.toDomain(repository.save(mapper.toEntity(book)));
    }

    @Override
    public Book update(Book book) {
        return mapper.toDomain(repository.save(mapper.toEntity(book)));
    }

    @Override
    public void delete(BookId bookId) {
        repository.deleteById(bookId.getValue());
    }

    @Override
    public Optional<Book> findById(BookId bookId) {
        return repository.findById(bookId.getValue()).map(mapper::toDomain);
    }

    @Override
    public List<Book> findBooksWithoutUserBookStateRecords(UserId userId) {
        return repository.findBooksWithoutUserBookStateRecords(userId.getValue()).stream().map(mapper::toDomain).toList();
    }

    @Override
    public PageResult<Book> findAll(PageQuery pageQuery) {
        Page<BookJpaEntity> result = repository.findAll(PageRequest.of(pageQuery.page(), pageQuery.size()));
        return mapPage(result);
    }

    @Override
    public PageResult<Book> searchByTitle(String keyword, PageQuery pageQuery) {
        Page<BookJpaEntity> result = repository.findByTitleContainingIgnoreCase(keyword, PageRequest.of(pageQuery.page(), pageQuery.size()));
        return mapPage(result);
    }

    private PageResult<Book> mapPage(Page<BookJpaEntity> result) {
        return new PageResult<>(
                result.getContent().stream().map(mapper::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast()
        );
    }
}
