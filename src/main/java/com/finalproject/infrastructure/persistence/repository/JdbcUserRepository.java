package com.finalproject.infrastructure.persistence.repository;

import com.finalproject.application.ports.output.repository.UserRepository;
import com.finalproject.domain.entity.User;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.infrastructure.persistence.config.DatabaseConfig;
import com.finalproject.infrastructure.persistence.mapper.JdbcUserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {
    private final DatabaseConfig databaseConfig;

    public JdbcUserRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Optional<User> findById(UserId userId) {
        String sql = """
                SELECT * 
                FROM userinfo 
                WHERE id = ?
                """;
        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(JdbcUserMapper.toUser(resultSet));
            }
            return Optional.empty();
        } catch (SQLException ex) {
            throw new RuntimeException("User fetch error", ex);
        }
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        String sql = """
                SELECT * 
                FROM userinfo
                 WHERE username = ? 
                    AND password = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(JdbcUserMapper.toUser(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by credentials", e);
        }
    }
} 