package com.finalproject.infrastructure.persistence.mapper;

import com.finalproject.domain.entity.Comment;
import com.finalproject.domain.valueobject.CommentId;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcCommentMapper {
    public static Comment toComment(ResultSet resultSet) throws SQLException {
        return new Comment(
                new CommentId(resultSet.getInt("id")),
                resultSet.getString("value"));
    }
}
