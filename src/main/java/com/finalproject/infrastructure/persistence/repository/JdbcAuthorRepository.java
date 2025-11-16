package com.finalproject.infrastructure.persistence.repository;

import com.finalproject.application.ports.output.repository.AuthorRepository;
import com.finalproject.domain.entity.Author;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.persistence.config.DatabaseConfig;
import com.finalproject.infrastructure.persistence.mapper.JdbcAuthorMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//todo avoid nulls ALL!
public class JdbcAuthorRepository implements AuthorRepository {
    private final DatabaseConfig databaseConfig;

    public JdbcAuthorRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public List<Author> findByName(String name) {
        String sql = """
                SELECT * 
                FROM authors 
                WHERE name = ?
                """;
        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                authors.add(JdbcAuthorMap.toAuthor(resultSet));
            }
            return authors;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding authors by name", e);
        }
    }

    @Override
    public List<Author> findWhichUserHasAtLeastThreeBooksRatedOver(UserId userId, Rating rating) {
        String sql = """
                    SELECT a.*, COUNT(b.id) as book_count 
                    FROM authors a
                    JOIN books b ON a.id = b.author_id
                    JOIN user_book_states ubs ON b.id = ubs.book_id
                    WHERE ubs.userinfo_id = ? AND ubs.rating >= ?
                    GROUP BY a.id
                    HAVING book_count >= 3
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId.getValue());
            preparedStatement.setInt(2, rating.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                authors.add(JdbcAuthorMap.toAuthor(resultSet));
            }
            return authors;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding favorite authors", e);
        }
    }

    @Override
    public Optional<Author> findByNameAndSurname(String authorName, String authorSurname) {
        String sql = """
                SELECT * 
                FROM authors 
                WHERE name = ? 
                  AND surname = ?
                """;
        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, authorName);
            preparedStatement.setString(2, authorSurname);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(JdbcAuthorMap.toAuthor(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding author by name and surname", e);
        }
    }

    @Override
    public Optional<Author> findByBookId(BookId bookId) {
        String sql = """
                SELECT a.* 
                FROM authors a 
                JOIN books b 
                    ON a.id = b.author_id 
                WHERE b.id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, bookId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(JdbcAuthorMap.toAuthor(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding author by book id", e);
        }
    }

    @Override
    public boolean existsByNameAndSurname(String name, String surname) {
        String sql = """
                SELECT COUNT(*) 
                FROM authors 
                WHERE name = ? 
                    AND surname = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking author existence", e);
        }
    }

    @Override
    public Author save(Author author) {
        String sql = """
                INSERT INTO authors (name, surname, website) 
                VALUES (?, ?, ?) 
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, author.getName());
            preparedStatement.setString(2, author.getSurname());
            preparedStatement.setString(3, Optional.ofNullable(author.getWebsite())
                    .map(Website::getUrl)
                    .orElse(null));

            preparedStatement.execute();

            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()")) {
                    if (resultSet.next()) {
                        return this.findById(new AuthorId(resultSet.getInt(1)))
                                .orElseThrow(() -> new RuntimeException("Failed to insert author"));
                    } else {
                        throw new RuntimeException("Failed to insert author");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving author", e);
        }
    }

    @Override
    public void deleteByNameAndSurname(String name, String surname) {
        String sql = """
                DELETE FROM authors 
                WHERE name = ? 
                    AND surname = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting author", e);
        }
    }

    @Override
    public Optional<Author> findById(AuthorId id) {
        String sql = """
                SELECT * 
                FROM authors 
                WHERE id = ?
                """;
        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(JdbcAuthorMap.toAuthor(resultSet));
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new RuntimeException("Error fetching author", ex);
        }
    }

    @Override
    public void deleteById(AuthorId authorId) {
        String sql = """
                DELETE FROM authors 
                WHERE id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, authorId.getValue());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting author", e);
        }
    }

    @Override
    public boolean hasMoreBooks(AuthorId authorId) {
        String sql = """
                SELECT COUNT(*) 
                FROM books 
                WHERE author_id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, authorId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking author's books", e);
        }
    }

    @Override
    public boolean hasMoreBooksExcluding(AuthorId authorId, BookId bookId) {
        String sql = """
                SELECT COUNT(*)
                FROM books b
                WHERE b.author_id = ? AND b.id != ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, authorId.getValue());
            preparedStatement.setInt(2, bookId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking author's other books", e);
        }
    }
}