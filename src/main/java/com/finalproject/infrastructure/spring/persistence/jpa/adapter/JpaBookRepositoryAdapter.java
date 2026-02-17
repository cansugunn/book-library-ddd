package com.finalproject.infrastructure.spring.persistence.jpa.adapter;

import com.finalproject.application.dto.page.PageQuery;
import com.finalproject.application.dto.page.PageResult;
import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.spring.persistence.jpa.entity.BookJpaEntity;
import com.finalproject.infrastructure.spring.persistence.jpa.repository.SpringDataBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaBookRepositoryAdapter implements BookRepository {
    private final SpringDataBookRepository repository;

    public JpaBookRepositoryAdapter(SpringDataBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean exists(BookId bookId) {
        return repository.existsById(bookId.getValue());
    }

    @Override
    public Book save(Book book) {
        return toDomain(repository.save(toEntity(book)));
    }

    @Override
    public Book update(Book book) {
        return toDomain(repository.save(toEntity(book)));
    }

    @Override
    public void delete(BookId bookId) {
        repository.deleteById(bookId.getValue());
    }

    @Override
    public Optional<Book> findById(BookId bookId) {
        return repository.findById(bookId.getValue()).map(this::toDomain);
    }

    @Override
    public List<Book> findBooksWithoutUserBookStateRecords(UserId userId) {
        return repository.findBooksWithoutUserBookStateRecords(userId.getValue()).stream().map(this::toDomain).toList();
    }

    @Override
    public PageResult<Book> findAll(PageQuery pageQuery) {
        Page<BookJpaEntity> result = repository.findAll(PageRequest.of(pageQuery.page(), pageQuery.size()));
        return new PageResult<>(
                result.getContent().stream().map(this::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast()
        );
    }

    private BookJpaEntity toEntity(Book book) {
        BookJpaEntity entity = new BookJpaEntity();
        if (book.getId() != null) {
            entity.setId(book.getId().getValue());
        }
        entity.setAuthorId(book.getAuthorId().getValue());
        entity.setTitle(book.getTitle());
        entity.setYear(book.getYear() == null ? null : book.getYear().getValue());
        entity.setNumberOfPages(book.getNumberOfPages() == null ? null : book.getNumberOfPages().getValue());
        entity.setCoverPath(book.getCover() == null ? null : book.getCover().getPath());
        entity.setAbout(book.getAbout());
        return entity;
    }

    private Book toDomain(BookJpaEntity entity) {
        return Book.Builder.newBuilder()
                .id(new BookId(entity.getId()))
                .author(new AuthorId(entity.getAuthorId()))
                .title(entity.getTitle())
                .year(entity.getYear() == null ? null : new Year(entity.getYear()))
                .numberOfPages(entity.getNumberOfPages() == null ? null : new NumberOfPages(entity.getNumberOfPages()))
                .cover(entity.getCoverPath() == null ? null : new Cover(entity.getCoverPath()))
                .about(entity.getAbout())
                .build();
    }
}
