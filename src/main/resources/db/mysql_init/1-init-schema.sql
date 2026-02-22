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


CREATE TABLE IF NOT EXISTS books_read_model (
    id BIGINT PRIMARY KEY,
    author_id BIGINT NOT NULL,
    author_name VARCHAR(100) NOT NULL,
    author_surname VARCHAR(100) NOT NULL,
    author_website VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    number_of_pages INT NOT NULL,
    cover_path VARCHAR(255),
    about TEXT
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

-- triggers for materialized views

DELIMITER $$

-- AFTER INSERT ON books
CREATE TRIGGER books_ai
    AFTER INSERT ON books
    FOR EACH ROW
BEGIN
    INSERT INTO books_read_model (
        id,
        author_id,
        author_name,
        author_surname,
        author_website,
        title,
        year,
        number_of_pages,
        cover_path,
        about
    )
    SELECT
        NEW.id,
        NEW.author_id,
        a.name,
        a.surname,
        a.website,
        NEW.title,
        NEW.year,
        NEW.number_of_pages,
        NEW.cover_path,
        NEW.about
    FROM authors a
    WHERE a.id = NEW.author_id;
    END$$


-- AFTER UPDATE ON books
CREATE TRIGGER books_au
AFTER UPDATE ON books
FOR EACH ROW
BEGIN
UPDATE books_read_model br
    JOIN authors a ON a.id = NEW.author_id
    SET
        br.author_id = NEW.author_id,
        br.author_name = a.name,
        br.author_surname = a.surname,
        br.author_website = a.website,
        br.title = NEW.title,
        br.year = NEW.year,
        br.number_of_pages = NEW.number_of_pages,
        br.cover_path = NEW.cover_path,
        br.about = NEW.about
WHERE br.id = NEW.id;
END$$


-- AFTER DELETE ON books
CREATE TRIGGER books_ad
AFTER DELETE ON books
FOR EACH ROW
BEGIN
DELETE FROM books_read_model
WHERE id = OLD.id;
END$$


-- AFTER UPDATE ON authors
CREATE TRIGGER authors_au
    AFTER UPDATE ON authors
    FOR EACH ROW
BEGIN
    UPDATE books_read_model
    SET
        author_name = NEW.name,
        author_surname = NEW.surname,
        author_website = NEW.website
    WHERE author_id = NEW.id;
    END$$

    DELIMITER ;