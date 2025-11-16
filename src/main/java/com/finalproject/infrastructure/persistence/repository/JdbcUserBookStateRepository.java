package com.finalproject.infrastructure.persistence.repository;

import com.finalproject.application.ports.output.repository.UserBookStateRepository;
import com.finalproject.domain.entity.Comment;
import com.finalproject.domain.entity.UserBookState;
import com.finalproject.domain.valueobject.*;
import com.finalproject.infrastructure.persistence.config.DatabaseConfig;
import com.finalproject.infrastructure.persistence.mapper.JdbcCommentMapper;
import com.finalproject.infrastructure.persistence.mapper.JdbcUserBookStatusMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//todo how comments aggregations etc must be done
public class JdbcUserBookStateRepository implements UserBookStateRepository {
    private final DatabaseConfig databaseConfig;

    public JdbcUserBookStateRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public Optional<UserBookState> findByBookIdAndUserId(BookId bookId, UserId userId) {
        String sql = """
                SELECT ubs.* 
                FROM user_book_states ubs
                WHERE ubs.book_id = ? AND ubs.userinfo_id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookId.getValue());
            preparedStatement.setInt(2, userId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                UserBookState userBookState = JdbcUserBookStatusMapper.toUserBookStatus(resultSet);
                userBookState.getComments().addAll(loadComments(connection, userBookState.getId()));
                return Optional.of(userBookState);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user book state", e);
        }
    }

    @Override
    public List<UserBookState> findRatedOver(UserId userId, Rating rating) {
        String sql = """
                SELECT ubs.* 
                FROM user_book_states ubs
                WHERE ubs.userinfo_id = ? and ubs.rating > ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId.getValue());
            preparedStatement.setInt(2, rating.getValue());

            ResultSet resultSet = preparedStatement.executeQuery();
            List<UserBookState> userBookStateList = new ArrayList<>();

            while (resultSet.next()) {
                UserBookState userBookState = JdbcUserBookStatusMapper.toUserBookStatus(resultSet);
                userBookState.getComments().addAll(loadComments(connection, userBookState.getId()));
                userBookStateList.add(JdbcUserBookStatusMapper.toUserBookStatus(resultSet));
            }
            return userBookStateList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user rated over books", e);
        }
    }

    @Override
    public List<UserBookState> findNotReadYetOf(UserId userId) {
        String sql = """
                SELECT ubs.* 
                FROM user_book_states ubs
                WHERE ubs.userinfo_id = ? and ubs.read_status != ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId.getValue());
            preparedStatement.setInt(2, Read.READ.getOrdinaryValue());

            ResultSet resultSet = preparedStatement.executeQuery();
            List<UserBookState> userBookStateList = new ArrayList<>();

            while (resultSet.next()) {
                UserBookState userBookState = JdbcUserBookStatusMapper.toUserBookStatus(resultSet);
                userBookState.getComments().addAll(loadComments(connection, userBookState.getId()));
                userBookStateList.add(JdbcUserBookStatusMapper.toUserBookStatus(resultSet));
            }
            return userBookStateList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user not read books", e);
        }
    }

    @Override
    public List<UserBookState> findToBeReadIn1Week(UserId userId) {
        String sql = """
                SELECT ubs.* 
                FROM user_book_states ubs 
                WHERE ubs.userinfo_id = ? AND ubs.read_status = ?  
                AND ubs.release_date BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY)
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId.getValue());
            preparedStatement.setInt(2, Read.WISH_TO_BE_READ.getOrdinaryValue());

            ResultSet resultSet = preparedStatement.executeQuery();
            List<UserBookState> userBookStateList = new ArrayList<>();

            while (resultSet.next()) {
                UserBookState userBookState = JdbcUserBookStatusMapper.toUserBookStatus(resultSet);
                userBookState.getComments().addAll(loadComments(connection, userBookState.getId()));
                userBookStateList.add(JdbcUserBookStatusMapper.toUserBookStatus(resultSet));
            }
            return userBookStateList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user books to be read in 1 week", e);
        }
    }

    @Override
    public UserBookState save(UserBookState userBookState) {
        String insertStateSql = """
                    INSERT INTO user_book_states (userinfo_id, book_id, read_status, rating, release_date)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertStateSql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, userBookState.getUserId().getValue());
            ps.setLong(2, userBookState.getBookId().getValue());

            if (userBookState.getRead() != null) {
                ps.setInt(3, userBookState.getRead().getOrdinaryValue());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (userBookState.getRating() != null) {
                ps.setInt(4, userBookState.getRating().getValue());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            if (userBookState.getReleaseDate() != null && userBookState.getReleaseDate().getDate() != null) {
                ps.setDate(5, new java.sql.Date(userBookState.getReleaseDate().getDate().getTime()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    UserBookStateId generatedId = new UserBookStateId(keys.getInt(1));
                    saveComments(connection, generatedId, userBookState.getComments());

                    return new UserBookState.Builder()
                            .id(generatedId)
                            .userId(userBookState.getUserId())
                            .bookId(userBookState.getBookId())
                            .read(userBookState.getRead())
                            .rating(userBookState.getRating())
                            .comments(userBookState.getComments())
                            .releaseDate(userBookState.getReleaseDate())
                            .build();
                } else {
                    throw new RuntimeException("Failed to get generated ID for UserBookState");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save UserBookState", e);
        }
    }


    @Override
    public UserBookState update(UserBookState userBookState) {
        String updateSql = """
                    UPDATE user_book_states
                    SET userinfo_id = ?, book_id = ?, read_status = ?, rating = ?, release_date = ?
                    WHERE id = ?
                """;

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSql)) {

            ps.setLong(1, userBookState.getUserId().getValue());
            ps.setLong(2, userBookState.getBookId().getValue());

            if (userBookState.getRead() != null) {
                ps.setInt(3, userBookState.getRead().getOrdinaryValue());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (userBookState.getRating() != null) {
                ps.setInt(4, userBookState.getRating().getValue());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            if (userBookState.getReleaseDate() != null  && userBookState.getReleaseDate().getDate() != null) {
                ps.setDate(5, new java.sql.Date(userBookState.getReleaseDate().getDate().getTime()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setLong(6, userBookState.getId().getValue());

            ps.executeUpdate();

            deleteCommentsByUserBookStateId(connection, userBookState.getId());
            saveComments(connection, userBookState.getId(), userBookState.getComments());

            return userBookState;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update UserBookState" + e.getMessage(), e);
        }
    }

    @Override
    public void deleteByBookId(BookId bookId) {
        String sql = """
                DELETE FROM user_book_states ubs 
                WHERE ubs.book_id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookId.getValue());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting user book state " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean exists(UserBookStateId userBookStateId) {
        String sql = """
                SELECT COUNT(*)
                 FROM user_book_states 
                 WHERE id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userBookStateId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking book existence", e);
        }
    }

    @Override
    public Optional<UserBookState> findById(UserBookStateId userBookStateId) {
        String sql = """
                SELECT * 
                FROM user_book_states ubs 
                WHERE ubs.id = ?
                """;

        Connection connection = databaseConfig.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userBookStateId.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                UserBookState userBookState = JdbcUserBookStatusMapper.toUserBookStatus(resultSet);
                userBookState.getComments().addAll(loadComments(connection, userBookState.getId()));
                return Optional.of(userBookState);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding book by id", e);
        }
    }

    private void saveComments(Connection connection, UserBookStateId userBookStateId, List<Comment> comments) throws SQLException {
        if (comments == null || comments.isEmpty()) return;

        String insertCommentSql = "INSERT INTO comments (user_book_state_id, value) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertCommentSql)) {
            for (Comment comment : comments) {
                ps.setLong(1, userBookStateId.getValue());
                ps.setString(2, comment.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteCommentsByUserBookStateId(Connection connection, UserBookStateId id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM comments WHERE user_book_state_id = ?")) {
            ps.setLong(1, id.getValue());
            ps.executeUpdate();
        }
    }

    private List<Comment> loadComments(Connection connection, UserBookStateId id) throws SQLException {
        String sql = "SELECT * FROM comments WHERE user_book_state_id = ? order by id desc";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id.getValue());
            ResultSet rs = ps.executeQuery();
            List<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                comments.add(JdbcCommentMapper.toComment(rs));
            }
            return comments;
        }
    }
} 