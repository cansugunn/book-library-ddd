package com.finalproject.infrastructure.persistence.mapper;

import com.finalproject.domain.entity.Book;
import com.finalproject.domain.valueobject.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcBookMapper {
    public static Book toBook(ResultSet resultSet) throws SQLException {
        return new Book(
                new BookId(resultSet.getInt("id")),
                new AuthorId(resultSet.getInt("author_id")),
                resultSet.getString("title"),
                new Year(resultSet.getInt("year")),
                new NumberOfPages(resultSet.getInt("number_of_pages")),
                new Cover(resultSet.getString("cover_path")),
                resultSet.getString("about")
        );
    }
}
