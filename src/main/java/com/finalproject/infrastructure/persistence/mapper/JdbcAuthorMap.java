package com.finalproject.infrastructure.persistence.mapper;

import com.finalproject.domain.entity.Author;
import com.finalproject.domain.valueobject.AuthorId;
import com.finalproject.domain.valueobject.Website;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcAuthorMap {
    public static Author toAuthor(ResultSet resultSet) throws SQLException {
        return new Author.Builder()
                .id(new AuthorId(resultSet.getInt("id")))
                .name(resultSet.getString("name"))
                .surname(resultSet.getString("surname"))
                .website(new Website(resultSet.getString("website")))
                .build();
    }
}
