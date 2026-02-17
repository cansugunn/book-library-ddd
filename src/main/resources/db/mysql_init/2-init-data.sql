INSERT INTO authors (name, surname, website)
VALUES ('George', 'Orwell', 'website-1');
INSERT INTO authors (name, surname, website)
VALUES ('Jane', 'Austen', 'website-2');
INSERT INTO authors (name, surname, website)
VALUES ('Fyodor', 'Dostoevsky', 'website-3');
INSERT INTO authors (name, surname, website)
VALUES ('J.K.', 'Rowling', 'website-4');
INSERT INTO authors (name, surname, website)
VALUES ('Ernest', 'Hemingway', 'website-5');
INSERT INTO authors (name, surname, website)
VALUES ('Agatha', 'Christie', 'website-6');
INSERT INTO authors (name, surname, website)
VALUES ('Mark', 'Twain', 'website-7');
INSERT INTO authors (name, surname, website)
VALUES ('Leo', 'Tolstoy', 'website-8');
INSERT INTO authors (name, surname, website)
VALUES ('Harper', 'Lee', 'website-9');
INSERT INTO authors (name, surname, website)
VALUES ('J.R.R.', 'Tolkien', 'website-10');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (6, '1984', 1990, 307, 'src/main/resources/covers/Book1.jpg', 'About 1984');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (5, 'Pride and Prejudice', 1971, 554, 'src/main/resources/covers/Book2.jpg', 'About Pride and Prejudice');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (2, 'Crime and Punishment', 1875, 489, 'src/main/resources/covers/Book3.jpg', 'About Crime and Punishment');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (8, 'Harry Potter', 1984, 342, 'src/main/resources/covers/Book4.jpg', 'About Harry Potter');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (8, 'The Old Man and the Sea', 2009, 209, 'src/main/resources/covers/Book5.jpg',
        'About The Old Man and the Sea');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (2, 'Murder on the Orient Express', 1858, 474, 'src/main/resources/covers/Book6.jpg',
        'About Murder on the Orient Express');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (1, 'Adventures of Huckleberry Finn', 1916, 413, 'src/main/resources/covers/Book7.jpg',
        'About Adventures of Huckleberry Finn');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (5, 'War and Peace', 1859, 625, 'src/main/resources/covers/Book8.jpg', 'About War and Peace');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (6, 'To Kill a Mockingbird', 1928, 661, NULL, 'About To Kill a Mockingbird');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (10, 'The Hobbit', 1977, 449, NULL, 'About The Hobbit');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (10, 'Anna Karenina', 1948, 525, NULL, 'About Anna Karenina');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (6, 'The Great Gatsby', 1992, 456, NULL, 'About The Great Gatsby');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (1, 'Brave New World', 1880, 369, NULL, 'About Brave New World');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (6, 'Fahrenheit 451', 2022, 161, NULL, 'About Fahrenheit 451');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (6, 'Jane Eyre', 1908, 307, NULL, 'About Jane Eyre');
INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES (7, 'Wuthering Heights', 1856, 178, NULL, 'About Wuthering Heights');
INSERT INTO userinfo (username, password, type)
VALUES ('alice', 'jiPZhSe6GrQkzlx8E+iLDA==', 1);
INSERT INTO userinfo (username, password, type)
VALUES ('bob', 'JIAEu2kRom7VHmfdnhahvg==', 2);
INSERT INTO userinfo (username, password, type)
VALUES ('charlie', 'eYwmQ6Pq0Yf1ceKiypseBA==', 1);
INSERT INTO userinfo (username, password, type)
VALUES ('dave', 'BM7atVbOWtxp3w/2gdujEg==', 2);
INSERT INTO userinfo (username, password, type)
VALUES ('eve', 'EqxE5GvvTC7R1MIDVOCHzA==', 1);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 1, 3, 0, '2025-06-03');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 2, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 3, 1, 3, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 4, 3, 0, '2025-06-09');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 5, 1, 3, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 6, 3, 0, '2025-06-09');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 7, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 8, 3, 0, '2025-06-03');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 9, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 10, 1, 3, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 11, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 12, 3, 0, '2025-06-13');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 13, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 14, 1, 2, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 15, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (1, 16, 1, 2, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 1, 1, 3, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 2, 1, 4, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 3, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 4, 1, 2, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 5, 1, 1, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 6, 1, 4, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 7, 1, 3, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 8, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 9, 3, 0, '2025-06-15');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 10, 3, 0, '2025-06-05');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 11, 1, 1, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 12, 3, 0, '2025-06-16');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 13, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 14, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 15, 3, 0, '2025-06-06');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (2, 16, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 1, 3, 0, '2025-06-11');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 2, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 3, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 4, 1, 1, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 5, 1, 1, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 6, 3, 0, '2025-06-11');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 7, 1, 1, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 8, 1, 1, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 9, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 10, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 11, 1, 3, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 12, 1, 3, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 13, 3, 0, '2025-06-04');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 14, 1, 4, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 15, 3, 0, '2025-06-13');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (3, 16, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 1, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 2, 1, 1, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 3, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 4, 1, 5, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 5, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 6, 1, 2, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 7, 1, 5, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 8, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 9, 3, 0, '2025-06-13');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 10, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 11, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 12, 1, 1, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 13, 3, 0, '2025-06-10');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 14, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 15, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (4, 16, 1, 2, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 1, 3, 0, '2025-06-05');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 2, 3, 0, '2025-06-10');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 3, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 4, 3, 0, '2025-06-06');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 5, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 6, 3, 0, '2025-06-10');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 7, 3, 0, '2025-06-04');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 8, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 9, 3, 0, '2025-06-07');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 10, 1, 5, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 11, 1, 5, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 12, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 13, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 14, 3, 0, '2025-06-13');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 15, 2, 0, NULL);
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES (5, 16, 1, 5, NULL);
INSERT INTO comments (user_book_state_id, value)
VALUES (1, 'Comment on book 1 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (2, 'Comment on book 2 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (3, 'Comment on book 3 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (4, 'Comment on book 4 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (5, 'Comment on book 5 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (6, 'Comment on book 6 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (7, 'Comment on book 7 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (8, 'Comment on book 8 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (9, 'Comment on book 9 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (10, 'Comment on book 10 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (11, 'Comment on book 11 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (12, 'Comment on book 12 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (13, 'Comment on book 13 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (14, 'Comment on book 14 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (15, 'Comment on book 15 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (16, 'Comment on book 16 by user 1');
INSERT INTO comments (user_book_state_id, value)
VALUES (17, 'Comment on book 1 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (18, 'Comment on book 2 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (19, 'Comment on book 3 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (20, 'Comment on book 4 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (21, 'Comment on book 5 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (22, 'Comment on book 6 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (23, 'Comment on book 7 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (24, 'Comment on book 8 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (25, 'Comment on book 9 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (26, 'Comment on book 10 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (27, 'Comment on book 11 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (28, 'Comment on book 12 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (29, 'Comment on book 13 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (30, 'Comment on book 14 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (31, 'Comment on book 15 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (32, 'Comment on book 16 by user 2');
INSERT INTO comments (user_book_state_id, value)
VALUES (33, 'Comment on book 1 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (34, 'Comment on book 2 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (35, 'Comment on book 3 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (36, 'Comment on book 4 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (37, 'Comment on book 5 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (38, 'Comment on book 6 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (39, 'Comment on book 7 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (40, 'Comment on book 8 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (41, 'Comment on book 9 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (42, 'Comment on book 10 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (43, 'Comment on book 11 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (44, 'Comment on book 12 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (45, 'Comment on book 13 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (46, 'Comment on book 14 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (47, 'Comment on book 15 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (48, 'Comment on book 16 by user 3');
INSERT INTO comments (user_book_state_id, value)
VALUES (49, 'Comment on book 1 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (50, 'Comment on book 2 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (51, 'Comment on book 3 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (52, 'Comment on book 4 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (53, 'Comment on book 5 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (54, 'Comment on book 6 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (55, 'Comment on book 7 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (56, 'Comment on book 8 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (57, 'Comment on book 9 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (58, 'Comment on book 10 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (59, 'Comment on book 11 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (60, 'Comment on book 12 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (61, 'Comment on book 13 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (62, 'Comment on book 14 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (63, 'Comment on book 15 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (64, 'Comment on book 16 by user 4');
INSERT INTO comments (user_book_state_id, value)
VALUES (65, 'Comment on book 1 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (66, 'Comment on book 2 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (67, 'Comment on book 3 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (68, 'Comment on book 4 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (69, 'Comment on book 5 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (70, 'Comment on book 6 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (71, 'Comment on book 7 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (72, 'Comment on book 8 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (73, 'Comment on book 9 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (74, 'Comment on book 10 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (75, 'Comment on book 11 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (76, 'Comment on book 12 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (77, 'Comment on book 13 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (78, 'Comment on book 14 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (79, 'Comment on book 15 by user 5');
INSERT INTO comments (user_book_state_id, value)
VALUES (80, 'Comment on book 16 by user 5');


INSERT INTO books (author_id, title, year, number_of_pages, cover_path, about)
VALUES
    (6, 'And Then There Were None', 1939, 272, NULL, 'About And Then There Were None'),
    (6, 'Death on the Nile', 1937, 345, NULL, 'About Death on the Nile');
INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
VALUES
    (2, 17, 1, 5, NULL),
    (2, 18, 1, 5, NULL);