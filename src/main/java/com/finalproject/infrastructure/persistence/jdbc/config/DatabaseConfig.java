package com.finalproject.infrastructure.persistence.jdbc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String SWING_DB_URL = env("SWING_DB_URL", "jdbc:mysql://localhost:3306/booklibrary");
    private static final String SWING_DB_USER = env("SWING_DB_USER", "root");
    private static final String SWING_DB_PASSWORD = env("SWING_DB_PASSWORD", "password");
    private static final String SWING_DB_DRIVER = env("SWING_DB_DRIVER", "com.mysql.cj.jdbc.Driver");

    private static final String FALLBACK_URL = "jdbc:h2:mem:mylibrary;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;NON_KEYWORDS=YEAR,VALUE;INIT=RUNSCRIPT FROM 'classpath:db/h2/schema.sql'\\;RUNSCRIPT FROM 'classpath:db/h2/data.sql'";
    private static final String FALLBACK_USER = "sa";
    private static final String FALLBACK_PASSWORD = "";
    private static final String FALLBACK_DRIVER = "org.h2.Driver";

    private static DatabaseConfig instance;

    private final String jdbcUrl;
    private final String user;
    private final String password;
    private final String driverClassName;
    private Connection connection;

    private DatabaseConfig() {
        DbSettings settings = resolveSettings();
        this.jdbcUrl = settings.url();
        this.user = settings.user();
        this.password = settings.password();
        this.driverClassName = settings.driver();

        try {
            Class.forName(driverClassName);
            this.connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(jdbcUrl, user, password);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    public void shutdown() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error while closing DB connection: " + e.getMessage());
        }
    }

    private static DbSettings resolveSettings() {
        try {
            Class.forName(SWING_DB_DRIVER);
            return new DbSettings(SWING_DB_URL, SWING_DB_USER, SWING_DB_PASSWORD, SWING_DB_DRIVER);
        } catch (ClassNotFoundException ignored) {
            return new DbSettings(FALLBACK_URL, FALLBACK_USER, FALLBACK_PASSWORD, FALLBACK_DRIVER);
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }

    private record DbSettings(String url, String user, String password, String driver) {
    }
}
