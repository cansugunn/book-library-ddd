package com.finalproject.infrastructure.persistence.mapper;

import com.finalproject.domain.entity.UserBookState;
import com.finalproject.domain.valueobject.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

//todo how nullability must be controlled_?
public class JdbcUserBookStatusMapper {
    public static UserBookState toUserBookStatus(ResultSet resultSet) throws SQLException {
        int userBookStateId = resultSet.getInt("id");
        return new UserBookState.Builder()
                .id(new UserBookStateId(userBookStateId))
                .bookId(new BookId(resultSet.getInt("book_id")))
                .userId(new UserId(resultSet.getInt("userinfo_id")))
                .read(Read.of(resultSet.getInt("read_status")))
                .rating(new Rating(resultSet.getInt("rating")))
                .comments(new LinkedList<>())
                .releaseDate(resultSet.getDate("release_date") != null ? new ReleaseDate(resultSet.getDate("release_date")) : null)
                .build();
    }
}
