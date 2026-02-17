package com.finalproject.infrastructure.swing.persistence.jdbc.repository;

import com.finalproject.application.ports.output.repository.UnitOfWork;
import com.finalproject.infrastructure.swing.persistence.jdbc.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class JdbcUnitOfWork implements UnitOfWork {
    private final DatabaseConfig databaseConfig;

    public JdbcUnitOfWork(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    @Override
    public <T> T executeInTransaction(Supplier<T> action) {
        try (Connection connection = databaseConfig.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T result = action.get();
                connection.commit();
                return result;
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot obtain DB connection", e);
        }
    }
}
