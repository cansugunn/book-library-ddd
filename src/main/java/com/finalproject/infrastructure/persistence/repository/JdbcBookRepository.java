package com.finalproject.infrastructure.persistence.repository;

import com.finalproject.application.ports.output.repository.BookRepository;
import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.BookId;
import com.finalproject.domain.valueobject.Cover;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.infrastructure.persistence.config.DatabaseConfig;
import com.finalproject.infrastructure.persistence.mapper.JdbcBookMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBookRepository implements BookRepository {
    private final DatabaseConfig databaseConfig;

    public JdbcBookRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Book save(Book book) {
        String sql = """
                INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about) 
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, book.getAuthorId().getValue());
            preparedStatement.setString(2, book.getTitle());
            if (book.getYear() != null) {
                preparedStatement.setInt(3, book.getYear().getValue());
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
            }
            if (book.getNumberOfPages() != null) {
                preparedStatement.setInt(4, book.getNumberOfPages().getValue());
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            }
            if (book.getCover() != null && book.getCover().getPath() != null) {
                preparedStatement.setString(5, book.getCover().getPath());
            } else {
                preparedStatement.setNull(5, java.sql.Types.VARCHAR);
            }
            preparedStatement.setString(6, book.getAbout());

            preparedStatement.execute();
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()")) {
                    if (resultSet.next()) {
                        return this.findById(new BookId(resultSet.getInt(1)))
                                .orElseThrow(() -> new RuntimeException("Failed to insert book"));
                    } else {
                        throw new RuntimeException("Failed to insert book");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving book", e);
        }
    }

    @Override
    public Book update(Book book) {
        String sql = """
                UPDATE books 
                SET author_id = ?, title = ?, year = ?, number_of_pages = ?, cover_path = ?, about = ? 
                WHERE id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, book.getAuthorId().getValue());
            preparedStatement.setString(2, book.getTitle());
            if (book.getYear() != null) {
                preparedStatement.setInt(3, book.getYear().getValue());
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
            }
            if (book.getNumberOfPages() != null) {
                preparedStatement.setInt(4, book.getNumberOfPages().getValue());
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            }
            preparedStatement.setString(5, Optional.ofNullable(book.getCover())
                    .map(Cover::getPath)
                    .orElse(null));
            preparedStatement.setString(6, book.getAbout());
            preparedStatement.setInt(7, book.getId().getValue());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            return book;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating book", e);
        }
    }

    @Override
    public Optional<Book> findById(BookId bookId) {
        String sql = """
                SELECT * 
                FROM books 
                WHERE id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, bookId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(JdbcBookMapper.toBook(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding book by id", e);
        }
    }

    @Override
    public List<Book> findBooksWithoutUserBookStateRecords(UserId userId) {
        String sql = """
                SELECT * 
                FROM books b
                LEFT JOIN user_book_states ubs 
                    ON b.id = ubs.book_id 
                    AND ubs.userinfo_id = ?
                WHERE ubs.userinfo_id IS NULL
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Book> bookList = new ArrayList<>();
            while (resultSet.next()) {
                bookList.add(JdbcBookMapper.toBook(resultSet));
            }
            return bookList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding book by id", e);
        }
    }

    @Override
    public void delete(BookId bookId) {
        String sql = """
                DELETE FROM books 
                WHERE id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, bookId.getValue());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting book", e);
        }
    }

    @Override
    public boolean exists(BookId bookId) {
        String sql = """
                SELECT COUNT(*)
                 FROM books 
                 WHERE id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, bookId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking book existence", e);
        }
    }
} 