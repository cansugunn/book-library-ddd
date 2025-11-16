CREATE SCHEMA IF NOT EXISTS mylibrary;

USE mylibrary;

CREATE TABLE IF NOT EXISTS authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    website VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    author_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    number_of_pages INT NOT NULL,
    cover_path VARCHAR(255),
    about TEXT,
    FOREIGN KEY (author_id) REFERENCES authors(id)
    );

CREATE TABLE IF NOT EXISTS userinfo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    type INT NOT NULL CHECK (type BETWEEN 1 AND 2)
);

CREATE TABLE IF NOT EXISTS user_book_states (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userinfo_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    read_status INT NOT NULL CHECK (read_status BETWEEN 1 AND 3),
    rating INT NOT NULL CHECK (rating BETWEEN 0 AND 5),
    release_date DATE,
    FOREIGN KEY (userinfo_id) REFERENCES userinfo(id),
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_book_state_id BIGINT NOT NULL,
    value TEXT NOT NULL,
    FOREIGN KEY (user_book_state_id) REFERENCES user_book_states(id) ON DELETE CASCADE
);
