package com.finalproject.infrastructure.persistence.mapper;

import com.finalproject.domain.entity.User;
import com.finalproject.domain.valueobject.UserId;
import com.finalproject.domain.valueobject.UserType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserMapper {
    public static User toUser(ResultSet resultSet) throws SQLException {
        return new User(
                new UserId(resultSet.getInt("id")),
                resultSet.getString("username"),
                resultSet.getString("password"),
                UserType.of(resultSet.getInt("type")));
    }
}
